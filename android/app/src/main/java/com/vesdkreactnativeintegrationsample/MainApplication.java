package com.vesdkreactnativeintegrationsample;

import android.app.Application;
import android.content.Context;

import com.banuba.sdk.arcloud.di.ArCloudKoinModule;
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule;
import com.banuba.sdk.gallery.di.GalleryKoinModule;
import com.banuba.sdk.token.storage.di.TokenStorageKoinModule;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import com.vesdkreactnativeintegrationsample.generated.BasePackageList;
import com.vesdkreactnativeintegrationsample.videoeditor.di.VideoEditorKoinModule;

import org.koin.core.context.GlobalContext;
import org.unimodules.adapters.react.ModuleRegistryAdapter;
import org.unimodules.adapters.react.ReactModuleRegistryProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.annotation.Nullable;

import expo.modules.updates.UpdatesController;

import static org.koin.android.ext.koin.KoinExtKt.androidContext;
import static org.koin.core.context.GlobalContextExtKt.startKoin;

public class MainApplication extends Application implements ReactApplication {
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
      packages.add(new ActivityStarterReactPackage());
      packages.add(new ModuleRegistryAdapter(mModuleRegistryProvider));
      return packages;
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }

    @Override
    protected @Nullable String getJSBundleFile() {
      if (BuildConfig.DEBUG) {
        return super.getJSBundleFile();
      } else {
        return UpdatesController.getInstance().getLaunchAssetFile();
      }
    }

    @Override
    protected @Nullable String getBundleAssetName() {
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

    // Init Banuba VE SDK
    startKoin(GlobalContext.INSTANCE, koinApplication -> {
            androidContext(koinApplication, this);
            koinApplication.modules(
                    new VideoEditorKoinModule().getModule(),
                    new ArCloudKoinModule().getModule(),
                    new BanubaEffectPlayerKoinModule().getModule(),
                    new TokenStorageKoinModule().getModule(),
                    new GalleryKoinModule().getModule());
            return null;
        });
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
