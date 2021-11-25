//
//  VideoEditorModule.swift
//  vesdkreactnativeintegrationsample
//
//  Created by Andrei Sak on 28.12.20.
//

import React
import BanubaVideoEditorSDK
import BanubaMusicEditorSDK
import BanubaOverlayEditorSDK
import VideoEditor

@objc(VideoEditorModule)
class VideoEditorModule: NSObject, RCTBridgeModule {
  
  private var videoEditorSDK: BanubaVideoEditor?
  
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  // Result urls callback
  var exportCallback: RCTResponseSenderBlock?
  
  @objc func openVideoEditor() {
    let config = createVideoEditorConfiguration()
    videoEditorSDK = BanubaVideoEditor(
      token: "Place your video editor token here",
      configuration: config,
      externalViewControllerFactory: nil
    )
    
    // Set delegate
    videoEditorSDK?.delegate = self
    
    DispatchQueue.main.async {
      guard let presentedVC = RCTPresentedViewController() else {
        return
      }
      self.videoEditorSDK?.presentVideoEditor(from: presentedVC, animated: true, completion: nil)
    }
  }
  
  private func createVideoEditorConfiguration() -> VideoEditorConfig {
    let config = VideoEditorConfig()
    // Do customization here
    return config
  }
  
  // MARK: - RCTBridgeModule
  static func moduleName() -> String! {
    return "VideoEditorModule"
  }
}

// MARK: - Export flow
extension VideoEditorModule {
  func exportVideo() {
    let manager = FileManager.default
    // File name
    let firstFileURL = manager.temporaryDirectory.appendingPathComponent("tmp1.mov")
    if manager.fileExists(atPath: firstFileURL.path) {
      try? manager.removeItem(at: firstFileURL)
    }
    
    // Video configuration
    let exportVideoConfigurations: [ExportVideoConfiguration] = [
      ExportVideoConfiguration(
        fileURL: firstFileURL,
        quality: .auto,
        useHEVCCodecIfPossible: true,
        watermarkConfiguration: nil
      )
    ]
    
    // Export Configuration
    let exportConfiguration = ExportConfiguration(
      videoConfigurations: exportVideoConfigurations,
      isCoverEnabled: true,
      gifSettings: nil
    )
    
    // Export func
    videoEditorSDK?.export(
      using: exportConfiguration
    ) { [weak self] (success, error, coverImage) in
      // Export Callback
      DispatchQueue.main.async {
        if success {
          // Result urls. You could interact with your own implementation.
          let urls = exportVideoConfigurations.map { $0.fileURL }
          self?.exportCallback?(urls)
          // remove strong reference to video editor sdk instance
          self?.videoEditorSDK = nil
        } else {
          // remove strong reference to video editor sdk instance
          self?.videoEditorSDK = nil
          print("Error: \(String(describing: error))")
        }
      }
    }
  }
}

// MARK: - BanubaVideoEditorSDKDelegate
extension VideoEditorModule: BanubaVideoEditorDelegate {
  func videoEditorDidCancel(_ videoEditor: BanubaVideoEditor) {
    videoEditor.dismissVideoEditor(animated: true) {
      // remove strong reference to video editor sdk instance
      self.videoEditorSDK = nil
    }
  }
  
  func videoEditorDone(_ videoEditor: BanubaVideoEditor) {
    videoEditor.dismissVideoEditor(animated: true) {
      self.exportVideo()
    }
  }
}
