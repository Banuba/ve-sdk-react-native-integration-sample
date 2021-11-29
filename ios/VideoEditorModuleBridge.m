//
//  VideoEditorModuleBridge.m
//  vesdkreactnativeintegrationsample
//
//  Created by Andrei Sak on 28.12.20.
//

#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(VideoEditorModule, NSObject)

RCT_EXTERN_METHOD(openVideoEditor: (RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)

@end
