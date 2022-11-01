
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(VideoEditorModule, NSObject)

RCT_EXTERN_METHOD(openVideoEditor: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(openVideoEditorPIP: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

@end
