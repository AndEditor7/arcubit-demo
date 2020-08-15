package com.andedit.arcubit.util;

public class FileUtil 
{
	/** Change file extension. */
	public static String setExtension(String extension, String fileName)
	{
		int a = fileName.length();
		int num = 0;
		
		for (int i = a; i > 0; --i)
		{
			char data = fileName.charAt(i-1);
			if (data == '.')
			{
				fileName = StringUtils.inSubString(fileName, num);
				fileName += extension;
				return fileName;
			} else if (data == '\\' || data == '/')
			{
				fileName += "." + extension;
				return fileName;
			} else num++;
		}
		return fileName;
	}
	
	/** Get file extension. */
	public static String getExtension(String fileName)
	{
		int a = fileName.length();
		int num = 0;
		
		for (int i = a; i > 0; --i)
		{
			char data = fileName.charAt(i-1);
			if (data == '.') {
				return fileName.substring(a-num);
			} else if (data == '\\' || data == '/') return null;
			num++;
		}
		
		return null;	
	}
	
	/** Is extension match to file's extension. */
	public static boolean extensionOf(String extension, String fileName) {
		return StringUtils.matches(extension, getExtension(fileName));
	}
}
