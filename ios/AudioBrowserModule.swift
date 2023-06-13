
import Foundation
import BanubaVideoEditorSDK
import BanubaUtilities
import React

class AudioBrowserModule: UIViewController, TrackSelectionViewController, RCTBridgeModule {
      
  // MARK: - TrackSelectionViewController
  var trackSelectionDelegate: BanubaUtilities.TrackSelectionViewControllerDelegate?
  
  static func moduleName() -> String! {
    return "audio_browser"
  }
  
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  override func viewDidLoad() {
    let delegate = UIApplication.shared.delegate as! AppDelegate
    // Show custom audio browser screen implemented in JS
    self.view = RCTRootView(bridge: delegate.bridge!, moduleName: AudioBrowserModule.moduleName(), initialProperties: nil)
  }
}
