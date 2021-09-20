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

@objc(VideoEditorModule)
class VideoEditorModule: NSObject, RCTBridgeModule {
  
  private var videoEditorSDK: BanubaVideoEditor?
  
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
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

// MARK: - BanubaVideoEditorSDKDelegate
extension VideoEditorModule: BanubaVideoEditorDelegate {
  func videoEditorDidCancel(_ videoEditor: BanubaVideoEditor) {
    videoEditor.dismissVideoEditor(animated: true) {
      // remove strong reference to video editor sdk instance
      self.videoEditorSDK = nil
    }
  }
  
  func videoEditorDone(_ videoEditor: BanubaVideoEditor) {
    // Do export stuff here
  }
}
