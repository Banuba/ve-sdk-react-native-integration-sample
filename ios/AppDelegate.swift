//
//  ApplDelegate.swift
//  vesdkreactnativeintegrationsample
//
//

import Foundation
import React

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
  
  /*
   true - use custom audio browser implementation in this sample.
   false - use default default implementation.
   */
  static let configEnableCustomAudioBrowser = false
  
  // Set your Mubert Api key here
  static let mubertApiLicense = ""
  static let mubertApiKey = ""

  // Specify name of your project module
  private let moduleName = "main"
  private let rootPath = "index"

  var window: UIWindow?

  var bridge: RCTBridge!

  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    let jsCodeLocation: URL = RCTBundleURLProvider.sharedSettings().jsBundleURL(forBundleRoot: rootPath, fallbackResource: nil)

    let rootView = RCTRootView(bundleURL: jsCodeLocation, moduleName: moduleName, initialProperties: nil, launchOptions: launchOptions)
    let rootViewController = UIViewController()

    rootViewController.view = rootView

    self.window = UIWindow(frame: UIScreen.main.bounds)
    self.bridge = rootView.bridge
    self.window?.rootViewController = rootViewController
    self.window?.makeKeyAndVisible()
    return true
  }
}
