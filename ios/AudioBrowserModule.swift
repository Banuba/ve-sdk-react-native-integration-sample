//
//  AudioBrowserModule.swift
//  vesdkreactnativeintegrationsample
//
//  Created by Gleb Prischepa on 11/23/22.
//

import Foundation
import BanubaVideoEditorSDK
import BanubaMusicEditorSDK
import BanubaUtilities
import React

@objc(AudioBrowserModule)
class AudioBrowserModule: UIViewController, TrackSelectionViewController, RCTBridgeModule {
  
  let channelAudioBrowser = "audioBrowserChannel"
  let methodApplyAudioTrack = "applyAudioTrack"
  let methodDiscardAudioTrack = "discardAudioTrack"
  let methodClose = "close"
  
  // MARK: - TrackSelectionViewController
  var trackSelectionDelegate: BanubaMusicEditorSDK.TrackSelectionViewControllerDelegate?
  
  
  static func moduleName() -> String! {
    print("AudioBrowser: moduleName!")
    return "audio_browser"
  }
  
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  @objc
  func listenCalls() {
    print("AudioBrowser: listen!")
    guard let presentedVC = RCTPresentedViewController() else {
      return
    }
  }
  
  
}
