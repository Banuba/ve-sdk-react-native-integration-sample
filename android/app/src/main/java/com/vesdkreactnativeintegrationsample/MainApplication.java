package com.vesdkreactnativeintegrationsample;

import android.app.Application;
import android.content.Context;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import com.vesdkreactnativeintegrationsample.generated.BasePackageList;

import org.unimodules.adapters.react.ModuleRegistryAdapter;
import org.unimodules.adapters.react.ReactModuleRegistryProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Nullable;

import expo.modules.updates.UpdatesController;

import android.util.Log;
import com.banuba.sdk.token.storage.license.BanubaVideoEditor;
import com.banuba.sdk.token.storage.license.LicenseStateCallback;

public class MainApplication extends Application implements ReactApplication {

    /**
     * true - use custom audio browser implementation in this sample
     * false - use default implementation
     */
    public static final boolean USE_CUSTOM_AUDIO_BROWSER = false;

    private final String TAG = "BanubaVideoEditor";

    private final String LICENSE_TOKEN = SET YOUR LICENSE TOKEN;


    /* package */ static final String ERR_SDK_NOT_INITIALIZED
            = "Banuba Video Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba";
    /* package */ static final String ERR_LICENSE_REVOKED = "License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new";

    /* package */ BanubaVideoEditor videoEditorSDK;

    private final ReactModuleRegistryProvider mModuleRegistryProvider = new ReactModuleRegistryProvider(
            new BasePackageList().getPackageList()
    );

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            List<ReactPackage> packages = new PackageList(this).getPackages();
            packages.add(new VideoEditorReactPackage());
            packages.add(new ModuleRegistryAdapter(mModuleRegistryProvider));
            return packages;
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }

        @Override
        protected @Nullable
        String getJSBundleFile() {
            if (BuildConfig.DEBUG) {
                return super.getJSBundleFile();
            } else {
                return UpdatesController.getInstance().getLaunchAssetFile();
            }
        }

        @Override
        protected @Nullable
        String getBundleAssetName() {
            if (BuildConfig.DEBUG) {
                return super.getBundleAssetName();
            } else {
                return UpdatesController.getInstance().getBundleAssetName();
            }
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);

        if (!BuildConfig.DEBUG) {
            UpdatesController.initialize(this);
        }

        initializeFlipper(this, getReactNativeHost().getReactInstanceManager());

        videoEditorSDK = BanubaVideoEditor.Companion.initialize(LICENSE_TOKEN);

        if (videoEditorSDK == null) {
            // Token you provided is not correct - empty or truncated
            Log.e(TAG, ERR_SDK_NOT_INITIALIZED);
        } else {
            // Initialize Banuba VE UI SDK
            new BanubaVideoEditorSDK().initialize(this);
        }
    }

    /**
     * Loads Flipper in React Native templates. Call this in the onCreate method with something like
     * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
     *
     * @param context
     * @param reactInstanceManager
     */
    private static void initializeFlipper(
            Context context, ReactInstanceManager reactInstanceManager) {
        if (BuildConfig.DEBUG) {
            try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
                Class<?> aClass = Class.forName("com.vesdkreactnativeintegrationsample.ReactNativeFlipper");
                aClass
                        .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
                        .invoke(null, context, reactInstanceManager);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
