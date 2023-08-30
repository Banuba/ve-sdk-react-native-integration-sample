import React
import BanubaVideoEditorSDK
import VideoEditor
import VEExportSDK
import AVKit
import BanubaAudioBrowserSDK

typealias TimerOptionConfiguration = TimerConfiguration.TimerOptionConfiguration

@objc(VideoEditorModule)
class VideoEditorModule: NSObject, RCTBridgeModule {
  
  static let errEditorNotInitialized = "ERR_VIDEO_EDITOR_NOT_INITIALIZED"
  static let errEditorLicenseRevoked = "ERR_VIDEO_EDITOR_LICENSE_REVOKED"
  
  private let customViewControllerFactory = CustomViewControllerFactory()
  
  private var videoEditorSDK: BanubaVideoEditor?
  
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  private var currentResolve: RCTPromiseResolveBlock?
  private var currentReject: RCTPromiseRejectBlock?
  
  private var customAudioTrackUUID: UUID?

  @objc (initVideoEditor:resolver:rejecter:)
    func initVideoEditor(_ token: String, _ resolve: @escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
      let config = createVideoEditorConfiguration()


      videoEditorSDK = BanubaVideoEditor(
        token: token,
        configuration: config,
        externalViewControllerFactory: customViewControllerFactory
      )

      if videoEditorSDK == nil {
        reject(Self.errEditorNotInitialized, nil, nil)
        return
      }

      // Set delegate
      videoEditorSDK?.delegate = self
    }
  
  @objc func openVideoEditor(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    prepareAudioBrowser()
    
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
      
      self.checkLicenseAndStart(with: config, rejecter: reject)
    }
  }
  
  @objc func openVideoEditorPIP(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    prepareAudioBrowser()
    
    DispatchQueue.main.async {
      guard let presentedVC = RCTPresentedViewController() else {
        return
      }
      
      // sample_pip_video.mp4 file is hardcoded for demonstrating how to open video editor sdk in the simplest case.
      // Please provide valid video URL to open Video Editor in PIP.
      let pipVideoURL = Bundle.main.url(forResource: "sample_video", withExtension: "mp4")
      
      let pipLaunchConfig = VideoEditorLaunchConfig(
        entryPoint: .pip,
        hostController: presentedVC,
        pipVideoItem: pipVideoURL,
        musicTrack: nil,
        animated: true
      )
      
      self.checkLicenseAndStart(with: pipLaunchConfig, rejecter: reject)
    }
  }
  
  @objc func openVideoEditorTrimmer(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    prepareAudioBrowser()
    
    DispatchQueue.main.async {
      guard let presentedVC = RCTPresentedViewController() else {
        return
      }
      
      // sample_video.mp4 file is hardcoded for demonstrating how to open video editor sdk in the simplest case.
      // Please provide valid video URL to open Video Editor in Trimmer.
      let trimmerVideoURL = Bundle.main.url(forResource: "sample_video", withExtension: "mp4")!
      
      let trimmerLaunchConfig = VideoEditorLaunchConfig(
        entryPoint: .trimmer,
        hostController: presentedVC,
        videoItems: [trimmerVideoURL],
        musicTrack: nil,
        animated: true
      )
      
      self.checkLicenseAndStart(with: trimmerLaunchConfig, rejecter: reject)
    }
  }
  
  func checkLicenseAndStart(with config: VideoEditorLaunchConfig, rejecter reject: @escaping RCTPromiseRejectBlock) {
    if videoEditorSDK == nil {
      reject(Self.errEditorNotInitialized, nil, nil)
      return
    }
    
    // Checking the license might take around 1 sec in the worst case.
    // Please optimize use if this method in your application for the best user experience
    videoEditorSDK?.getLicenseState(completion: { [weak self] isValid in
      guard let self else { return }
      if isValid {
        print("✅ License is active, all good")
        DispatchQueue.main.async {
          self.videoEditorSDK?.presentVideoEditor(
            withLaunchConfiguration: config,
            completion: nil
          )
        }
      } else {
        self.videoEditorSDK = nil
        print("❌ License is either revoked or expired")
        reject(Self.errEditorLicenseRevoked, nil, nil)
      }
    })
  }
  
  // Applies audio track from custom audio browser
  @objc func applyAudioTrack(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    // Specify audio track URL. Video Editor SDK can apply tracks stored on the device.
    // In this sample we use audio file stored in the project.
    let audioURL = Bundle.main.url(forResource: "sample_audio", withExtension: "mp3")
    
    if (audioURL == nil) {
      let errMessage = "Failed to apply audio track. Unknow file"
      print(errMessage)
      self.currentReject!("", errMessage, nil)
      return
    }
    
    // Specify custom track name and additional data
    let trackName = "Track Name"
    let additionTitle = "Awesome artist"
    
    DispatchQueue.main.async {
      self.customAudioTrackUUID = UUID()
      let audioBrowserModule = self.getAudioBrowserModule()
      
      // Apply audio in Video Editor SDK
      audioBrowserModule.trackSelectionDelegate?.trackSelectionViewController(
        viewController: audioBrowserModule,
        didSelectFile: audioURL!,
        isEditable: true,
        title: trackName,
        additionalTitle: additionTitle,
        uuid: self.customAudioTrackUUID!
      )
      
      print("Audio track is applied")
      
      self.currentResolve!(nil)
    }
  }
  
  // Discards audio track in custom audio browser
  @objc func discardAudioTrack(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    DispatchQueue.main.async {
      let audioBrowserModule = self.getAudioBrowserModule()
      
      audioBrowserModule.trackSelectionDelegate?.trackSelectionViewControllerDiscardCurrentTrack(
        viewController: audioBrowserModule
      )
      
      print("Audio track is discarded")
      
      // Closes audio browser once track is discared. You can comment this line and avoid closing audio browser screen after discard.
      audioBrowserModule.trackSelectionDelegate?.trackSelectionViewControllerDidCancel(viewController: audioBrowserModule)
      
      self.currentResolve!(nil)
    }
  }
  
  // Closes audio browser
  @objc func closeAudioBrowser(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    self.currentResolve = resolve
    self.currentReject = reject
    
    DispatchQueue.main.async {
      let audioBrowserModule = self.getAudioBrowserModule()
      audioBrowserModule.trackSelectionDelegate?.trackSelectionViewControllerDidCancel(viewController: audioBrowserModule)
      
      self.currentResolve!(nil)
    }
  }
  
  // Prepares Audio Browser
  private func prepareAudioBrowser() {
    if (!AppDelegate.configEnableCustomAudioBrowser) {
      BanubaAudioBrowser.setMubertKeys(
        license: AppDelegate.mubertApiLicense,
        token: AppDelegate.mubertApiKey
      )
    }
  }
  
  private func getAudioBrowserModule() -> AudioBrowserModule {
    return (customViewControllerFactory.musicEditorFactory as! CustomAudioBrowserViewControllerFactory).audioBrowserModule!
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
  
  private func createVideoEditorConfiguration() -> VideoEditorConfig {
    var config = VideoEditorConfig()
    // Do customization here

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
      uuid: UUID(),
      id: nil,
      url: wavFile,
      coverURL: nil,
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
      exportProgress: nil
    ) { [weak self] (error, coverImage) in
      let success = error == nil
      // Export Callback
      DispatchQueue.main.async {
        if success {
          // Result urls. You could interact with your own implementation.
          
          self?.currentResolve?(["videoUri": firstFileURL.absoluteString])
          // remove strong reference to video editor sdk instance
          self?.videoEditorSDK = nil
          
          /*
           NOT REQUIRED FOR INTEGRATION
           Added for playing exported video file.
           */
          self?.demoPlayExportedVideo(videoURL: firstFileURL)
        } else {
          self?.currentReject?("", error?.errorMessage, nil)
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
      self?.currentResolve?(NSNull())
    }
  }
  
  func videoEditorDone(_ videoEditor: BanubaVideoEditor) {
    videoEditor.dismissVideoEditor(animated: true) { [weak self] in
      self?.exportVideo()
    }
  }
}

