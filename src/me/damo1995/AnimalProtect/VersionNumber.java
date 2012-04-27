package me.damo1995.AnimalProtect;

public class VersionNumber
{
    int[] version;

    public VersionNumber(String version)
    {
        // Split the input string by periods.
        String[] parts = version.split("\\.");

        // Initialize the field.
        this.version = new int[parts.length];

        // Populate the field.
        for (int i = 0; i < parts.length; i++)
            this.version[i] = Integer.valueOf(parts[i]);
    }
}