package com.heuristix;

import java.io.File;

public class DisplayableBytesButton extends DisplayableFileButton
{
  private byte[] bytes;

  public boolean updateFile(File file)
  {
    if (super.updateFile(file)) {
      byte[] bytes = Utilities.read(file);
      if (bytes != null) {
        updateButton(file.getName(), bytes);
        return true;
      }
    }
    return false;
  }

  public void updateButton(String name, byte[] bytes) {
    this.bytes = bytes;
    setText(name);
  }

  public byte[] getBytes() {
    return this.bytes;
  }
}