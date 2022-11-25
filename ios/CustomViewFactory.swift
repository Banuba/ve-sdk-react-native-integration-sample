//
//  CustomViewFactory.swift
//  vesdkreactnativeintegrationsample
//
//  Created by Gleb Prischepa on 11/23/22.
//

import BanubaVideoEditorSDK
import BanubaMusicEditorSDK
import BanubaUtilities
import Foundation

class CustomViewFactory: ExternalViewControllerFactory {
  
  // Set nil to use BanubaAudioBrowser
  var musicEditorFactory: MusicEditorExternalViewControllerFactory? = CustomAudioBrowserViewControllerFactory()
  
  var countdownTimerViewFactory: CountdownTimerViewFactory?
  
  var exposureViewFactory: AnimatableViewFactory?
}

class CustomAudioBrowserViewControllerFactory: MusicEditorExternalViewControllerFactory {
  
  // Audio Browser selection view controller
  func makeTrackSelectionViewController(selectedAudioItem: AudioItem?) -> TrackSelectionViewController? {
    print("AudioBrowser: make!")
    let controller = AudioBrowserModule(nibName: nil, bundle: nil)
    controller.listenCalls()
    return controller
  }
  
  // Effects selection view controller. Used at Music editor screen
  func makeEffectSelectionViewController(selectedAudioItem: BanubaUtilities.AudioItem?) -> BanubaMusicEditorSDK.EffectSelectionViewController? {
    return nil
  }
  
  // Returns recorder countdown view for voice recorder screen
  func makeRecorderCountdownAnimatableView() -> BanubaMusicEditorSDK.MusicEditorCountdownAnimatableView? {
    return nil
  }
}


