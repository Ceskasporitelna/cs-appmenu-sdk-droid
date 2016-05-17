package cz.csas.appmenu;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 23/05/16.
 */
public class AppVersion implements Comparable<AppVersion> {

    private Integer major;
    private Integer minor;

    /**
     * Version is required to be in a format Major.Minor.Build
     *
     * @param context
     * @throws PackageManager.NameNotFoundException
     */
    public AppVersion(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        String version = packageInfo.versionName;
        if (version != null) {
            String[] versions = version.split("\\.");
            if (versions.length >= 2) {
                major = Integer.parseInt(versions[0]);
                minor = Integer.parseInt(versions[1]);
            } else
                throw new CsAppMenuError(CsAppMenuError.Kind.OTHER, "App version could not be parsed. The required format is \"Major.Minor\"");
        }
    }

    public AppVersion(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppVersion)) return false;

        AppVersion that = (AppVersion) o;

        if (major != that.major) return false;
        return minor == that.minor;

    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        return result;
    }


    @Override
    public int compareTo(AppVersion appVersion) {
        if (this.equals(appVersion))
            return 0;
        if (appVersion.major == null || appVersion.minor == null || this.major > appVersion.major || this.major == appVersion.major && this.minor > appVersion.minor) {
            return 1;
        }
        return -1;
    }
}
