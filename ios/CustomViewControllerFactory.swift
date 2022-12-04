
import BanubaVideoEditorSDK
import BanubaMusicEditorSDK
import BanubaUtilities
import Foundation

// Factory is used to override visual representation of some video editor features.
class CustomViewControllerFactory: ExternalViewControllerFactory {
  
  // Override to use custom audio browser experience. Set nil to use default implementation
  var musicEditorFactory: MusicEditorExternalViewControllerFactory? = AppDelegate.useCustomAudioBrowser ? CustomAudioBrowserViewControllerFactory() : nil
  
  var countdownTimerViewFactory: CountdownTimerViewFactory?
  
  var exposureViewFactory: AnimatableViewFactory?
}

class CustomAudioBrowserViewControllerFactory: MusicEditorExternalViewControllerFactory {
  
  var audioBrowserModule: AudioBrowserModule?
  
  // Audio Browser selection view controller
  func makeTrackSelectionViewController(selectedAudioItem: AudioItem?) -> TrackSelectionViewController? {
    let module = AudioBrowserModule(nibName: nil, bundle: nil)
    audioBrowserModule = module
    return module
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


