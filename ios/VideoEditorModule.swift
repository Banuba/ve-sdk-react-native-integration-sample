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
import VEExportSDK

typealias TimerOptionConfiguration = TimerConfiguration.TimerOptionConfiguration

@objc(VideoEditorModule)
class VideoEditorModule: NSObject, RCTBridgeModule {
  
  private var videoEditorSDK: BanubaVideoEditor?
  
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  private var currentResolve: RCTPromiseResolveBlock?
  private var currentReject: RCTPromiseRejectBlock?
  
  @objc func openVideoEditor(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    initVideoEditor()
    
    DispatchQueue.main.async {
      guard let presentedVC = RCTPresentedViewController() else {
        return
      }
      var musicTrackPreset: MediaTrack?
      
      // uncomment this if you want to set the music track
      
      //musicTrackPreset = self.setupMusicTrackPresent()
      
      let config = VideoEditorLaunchConfig(
        entryPoint: .camera,
        hostController: presentedVC,
        musicTrack: musicTrackPreset,
        animated: true
      )
      self.videoEditorSDK?.presentVideoEditor(
        withLaunchConfiguration: config,
        completion: nil
      )
    }
  }
  
  @objc func openVideoEditorPIP(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    initVideoEditor()
    
    DispatchQueue.main.async {
      guard let presentedVC = RCTPresentedViewController() else {
        return
      }

      // sample_pip_video.mp4 file is hardcoded for demonstrating how to open video editor sdk in the simplest case.
      // Please provide valid video URL to open Video Editor in PIP.
      let pipVideoURL = Bundle.main.url(forResource: "sample_pip_video", withExtension: "mp4")
      
      let pipLaunchConfig = VideoEditorLaunchConfig(
                  entryPoint: .pip,
                  hostController: presentedVC,
                  pipVideoItem: pipVideoURL,
                  musicTrack: nil,
                  animated: true
              )
      
      self.videoEditorSDK?.presentVideoEditor(
        withLaunchConfiguration: pipLaunchConfig,
        completion: nil
      )
    }
  }
  
  private func initVideoEditor() {
    var config = createVideoEditorConfiguration()
    // Show mute audio button on Camera screen
    config.featureConfiguration.isMuteCameraAudioEnabled = true
    
    // Sets 3, 10 seconds timer for recording on Camera
    config.recorderConfiguration.timerConfiguration.options = [
      TimerOptionConfiguration(
        button: ImageButtonConfiguration(
          imageConfiguration: ImageConfiguration(imageName: "camera.time_effects_on")
        ),
        startingTimerSeconds: 3,
        stoppingTimerSeconds: .zero,
        description: String(format: NSLocalizedString("hands.free.seconds", comment: ""), "3")
      ),
      TimerOptionConfiguration(
        button: ImageButtonConfiguration(
          imageConfiguration: ImageConfiguration(imageName: "camera.time_effects_on")
        ),
        startingTimerSeconds: 10,
        stoppingTimerSeconds: .zero,
        description: String(format: NSLocalizedString("hands.free.seconds", comment: ""), "10")
      )
    ]
    
    videoEditorSDK = BanubaVideoEditor(
      token: /*@START_MENU_TOKEN@*/"SET BANUBA VIDEO EDITOR TOKEN"/*@END_MENU_TOKEN@*/,
      configuration: config,
      externalViewControllerFactory: nil
    )
    
    // Set delegate
    videoEditorSDK?.delegate = self
  }
  
  private func createVideoEditorConfiguration() -> VideoEditorConfig {
    let config = VideoEditorConfig()
    // Do customization here
    return config
  }
  // MARK: - Create music track
  private func setupMusicTrackPresent() -> MediaTrack {
    let documentsUrl = Bundle.main.bundleURL.appendingPathComponent("Music/long")
    let directoryContents = try? FileManager.default.contentsOfDirectory(at: documentsUrl, includingPropertiesForKeys: nil)
    let wavFile = directoryContents!.first(where: { $0.pathExtension == "wav" })!
    let urlAsset = AVURLAsset(url: wavFile)
    let urlAssetTimeRange = CMTimeRange(start: .zero, duration: urlAsset.duration)
    let mediaTrackTimeRange = MediaTrackTimeRange(
      startTime: .zero, playingTimeRange: urlAssetTimeRange
    )
    let musicTrackPreset = MediaTrack(
      id: 1231,
      url: wavFile,
      timeRange: mediaTrackTimeRange,
      isEditable: true,
      title: "test"
    )
    
    return musicTrackPreset
  }

  // MARK: - RCTBridgeModule
  static func moduleName() -> String! {
    return "VideoEditorModule"
  }
  
  /*
   NOT REQUIRED FOR INTEGRATION
   Added for playing exported video file.
   */
  func demoPlayExportedVideo(videoURL: URL) {
    
    guard let controller = RCTPresentedViewController() else {
      return
    }
    
    let player = AVPlayer(url: videoURL)
    let vc = AVPlayerViewController()
    vc.player = player
    
    controller.present(vc, animated: true) {
      vc.player?.play()
    }
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
      using: exportConfiguration,
      exportProgress: nil,
      completion: { success, error, exportCoverImages in
      // Export Callback
      DispatchQueue.main.async {
        if success {
          // Result urls. You could interact with your own implementation.
          self.currentResolve!(["videoUri": firstFileURL.absoluteString])
          // remove strong reference to video editor sdk instance
          self.videoEditorSDK = nil
          
          /*
           NOT REQUIRED FOR INTEGRATION
           Added for playing exported video file.
           */
          self.demoPlayExportedVideo(videoURL: firstFileURL)
        } else {
          self.currentReject!("", error?.errorMessage, nil)
          // remove strong reference to video editor sdk instance
          self.videoEditorSDK = nil
          print("Error: \(String(describing: error))")
        }
      }
    })
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
