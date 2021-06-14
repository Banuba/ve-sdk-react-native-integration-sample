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
  
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  @objc func openVideoEditor() {
    let config = createVideoEditorConfiguration()
    let videoEditor = BanubaVideoEditor(
      token: "Place your video editor token here",
      configuration: config,
      externalViewControllerFactory: nil
    )
    DispatchQueue.main.async {
      guard let presentedVC = RCTPresentedViewController() else {
        return
      }
      videoEditor.presentVideoEditor(from: presentedVC, animated: true, completion: nil)
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
