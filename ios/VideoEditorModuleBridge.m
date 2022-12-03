
#import <React/RCTBridgeModule.h>
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#import <React/RCTRootView.h>
#import <React/RCTUtils.h>
#import <React/RCTConvert.h>
#import <React/RCTBundleURLProvider.h>

@interface RCT_EXTERN_MODULE(VideoEditorModule, NSObject)

RCT_EXTERN_METHOD(openVideoEditor: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(openVideoEditorPIP: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

@end

@interface RCT_EXTERN_MODULE(AudioBrowserModule, NSObject)

@end
