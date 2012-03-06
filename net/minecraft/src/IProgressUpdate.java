package net.minecraft.src;

public interface IProgressUpdate
{
    /**
     * Shows the 'Saving level' string.
     */
    public abstract void displaySavingString(String s);

    public abstract void displayLoadingString(String s);

    public abstract void setLoadingProgress(int i);
}
