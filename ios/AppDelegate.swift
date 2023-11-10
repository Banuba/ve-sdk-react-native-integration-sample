//
//  ApplDelegate.swift
//  vesdkreactnativeintegrationsample
//
//

import Foundation
import React

@UIApplicationMain
class AppDelegate: RCTAppDelegate {
  
  /*
   true - use custom audio browser implementation in this sample.
   false - use default default implementation.
   */
  static let configEnableCustomAudioBrowser = false
  
  // Set your Mubert Api key here
  static let mubertApiLicense = ""
  static let mubertApiKey = ""

  override func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    moduleName = "vesdkreactnativeintegrationsample"
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
  
  override func sourceURL(for bridge: RCTBridge!) -> URL! {
  #if DEBUG
    return RCTBundleURLProvider.sharedSettings()?.jsBundleURL(forBundleRoot: "index")
  #else
    return Bundle.main.url(forResource: "main", withExtension: "jsbundle")
  #endif
  }
}
