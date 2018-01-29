package com.example.jun.myapplication.util;

import android.app.Activity;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.jun.myapplication.BuildConfig;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by JUN on 2018/1/8.
 */

public class PermissionUtils {

    /**
     * Build.MANUFACTURER
     */
    private static final String MANUFACTURER_HUAWEI = "HUAWEI";//华为
    private static final String MANUFACTURER_MEIZU = "Meizu";//魅族
    private static final String MANUFACTURER_XIAOMI = "Xiaomi";//小米
    private static final String MANUFACTURER_SONY = "Sony";//索尼
    private static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    private static final String MANUFACTURER_SMARTISANOS = "SmartisanOS";//锤子
    private static final String MANUFACTURER_LETV = "Letv";//乐视
    private static final String MANUFACTURER_ZTE = "ZTE";//中兴
    private static final String MANUFACTURER_YULONG = "YuLong";//酷派
    private static final String MANUFACTURER_LENOVO = "LENOVO";//联想

    /**
     * 此函数可以自己定义
     *
     * @param activity
     */
    public static void GoToSetting(Activity activity) {
        try {
            String systemRom = OSUtils.getRomType().toString();
            Toast.makeText(activity, "systemRom:" + systemRom, Toast.LENGTH_SHORT).show();
            switch (Build.MANUFACTURER) {
                case MANUFACTURER_HUAWEI:
                    Huawei(activity);
                    break;
                case MANUFACTURER_MEIZU:
                    Meizu(activity);
                    break;
                case MANUFACTURER_XIAOMI:
                    Xiaomi(activity);
                    break;
                case MANUFACTURER_SONY:
                    Sony(activity);
                    break;
                case MANUFACTURER_OPPO:
                    OPPO(activity);
                    break;
                case MANUFACTURER_LG:
                    LG(activity);
                    break;
                case MANUFACTURER_LETV:
                    Letv(activity);
                    break;
                default:
                    ApplicationInfo(activity);
                    Log.e("goToSetting", "目前暂不支持此系统，跳转至应用信息界面");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            SystemConfig(activity);
        }
    }

    /**
     * 华为
     *
     * @param activity
     */
    public static void Huawei(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ApplicationInfo(activity);
        }
    }

    /**
     * 魅族
     *
     * @param activity
     */
    public static void Meizu(Activity activity) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        activity.startActivity(intent);
    }

    /**
     * 小米
     *
     * @param activity
     */
    public static void Xiaomi(Activity activity) {
        String rom = getMiuiVersion();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if ("V5".equals(rom)) {
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        } else if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID);
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID);
        }
        activity.startActivity(intent);
    }

    /**
     * 索尼
     *
     * @param activity
     */
    public static void Sony(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * Oppo
     *
     * @param activity
     */
    public static void OPPO(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * LG
     *
     * @param activity
     */
    public static void LG(Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * 乐视
     *
     * @param activity
     */
    public static void Letv(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * 只能打开到自带安全软件
     *
     * @param activity
     */
    public static void _360(Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * 应用信息界面
     *
     * @param activity
     */
    public static void ApplicationInfo(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivity(localIntent);
    }

    /**
     * 系统设置界面
     *
     * @param activity
     */
    public static void SystemConfig(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }


    /**
     * 获取 MIUI 版本号
     */

    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

    /**
     * 判断是否具备所有权限
     *
     * @param permissions 所有权限
     * @return true 具有所有权限  false没有具有所有权限，此时包含未授予的权限
     */
    public static boolean isHasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        for (String permission : permissions) {
            if (!isHasPermission(context, permission))
                return false;
        }
        return true;
    }

    /**
     * 判断该权限是否已经被授予
     *
     * @param permission
     * @return true 已经授予该权限 ，false未授予该权限
     */
    private static boolean isHasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public static void requestPermissions(Context context, AcpListener acpListener, String... permissions) {
        Acp.getInstance(context).request(new AcpOptions.Builder()
                .setPermissions(permissions)
                .build(), acpListener);
    }

    /**
     * 请求权限,经测试发现TabActivity管理Activity时，在Activity中请求权限时需要传入父Activity对象，即TabActivity对象
     * 并在TabActivity管理Activity中重写onRequestPermissionsResult并分发到子Activity，否则回调不执行  。TabActivity回调中  调用getLocalActivityManager().getCurrentActivity().onRequestPermissionsResult(requestCode, permissions, grantResults);分发到子Activity
     *
     * @param object      Activity or Fragment
     * @param requestCode 请求码
     * @param permissions 请求权限
     */
    public static void requestPermissions(Context context, Object object, int requestCode, String... permissions) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String permission : permissions) {
            if (!isHasPermissions(context, permission)) {
                arrayList.add(permission);
            }
        }
        if (arrayList.size() > 0) {
            if (object instanceof Activity) {
                Activity activity = (Activity) object;
                Activity activity1 = activity.getParent() != null && activity.getParent() instanceof TabActivity ? activity.getParent() : activity;
                ActivityCompat.requestPermissions(activity1, arrayList.toArray(new String[]{}), requestCode);
            } else if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                //当Fragment嵌套Fragment时使用getParentFragment(),然后在父Fragment进行分发，否则回调不执行
                Fragment fragment1 = fragment.getParentFragment() != null ? fragment.getParentFragment() : fragment;
                fragment1.requestPermissions(arrayList.toArray(new String[]{}), requestCode);
            } else {
                throw new RuntimeException("the object must be Activity or Fragment");
            }
        }
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Object object, String... permissions) {
        for (String permission : permissions) {
            if (object instanceof Activity) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, permission)) {
                    return true;
                }
            } else if (object instanceof Fragment) {
                if (((Fragment) object).shouldShowRequestPermissionRationale(permission)) {
                    return true;
                }
            } else {
                throw new RuntimeException("the object must be Activity or Fragment");
            }


        }
        return false;
    }

    /**
     * 二次申请权限时，弹出自定义提示对话框
     *
     * @param activity
     * @param message
     * @param iPermissionRequest
     */
    public static void showPermissionDialog(Activity activity, String message, final IPermissionRequest iPermissionRequest) {
        new AlertDialog.Builder(activity)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        iPermissionRequest.agree();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        iPermissionRequest.refuse();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setMessage(message)
                .show();
    }


    public interface IPermissionRequest {
        void agree();

        void refuse();
    }

}
