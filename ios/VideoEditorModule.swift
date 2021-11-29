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
  
  private var currentResolve: RCTPromiseResolveBlock?
  private var currentReject: RCTPromiseRejectBlock?
  
  // Export callback
  @objc func openVideoEditor(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    let config = createVideoEditorConfiguration()
    videoEditorSDK = BanubaVideoEditor(
      token: "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",
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
          self?.currentResolve!(["videoUri": firstFileURL.absoluteString])
          // remove strong reference to video editor sdk instance
          self?.videoEditorSDK = nil
        } else {
          self?.currentReject!("", error?.errorMessage, nil)
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
    videoEditor.dismissVideoEditor(animated: true) { [weak self] in
      // remove strong reference to video editor sdk instance
      self?.videoEditorSDK = nil
      self?.currentResolve!(NSNull())
    }
  }
  
  func videoEditorDone(_ videoEditor: BanubaVideoEditor) {
    videoEditor.dismissVideoEditor(animated: true) { [weak self] in
      self?.exportVideo()
    }
  }
}
