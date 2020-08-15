package com.andedit.arcubit.util;

import com.badlogic.gdx.math.MathUtils;

/** Fast and extra String utilities. TODO: update this StringUtils class.
 * @author AndEditor7 */
public class StringUtils 
{		
	public static final String abc = "abcdefghijklmnopqrstuvwxyz";
	
	/** Like string.substring(number). but in reverse. */
	public static String inSubString(String string, int num)
	{
		if (num < 0) throw new StringIndexOutOfBoundsException(num);
		if (num == 0) return string;
		int a = string.length() - num;
		if (a < 1) return "";
		
		StringBuilder out = new StringBuilder(string.length());
		out.append(string);
		out.setLength(a);
		
		return out.toString();
	}
	
	/** Fast, simple strings matching and does'nt use regex. Its roundly 100 times (or more) faster! */
	public static boolean matches(String string1, String string2)
	{
		if (string2 == null || string1 == null) return false;
		int len = string1.length();
		if (len != string2.length()) return false;
		
		for (int i = 0; i < len; i++) 
		{
			if (string1.charAt(i) != string2.charAt(i))
				return false;
		}
		
		return true;
	}
	
	public static boolean isNumbers(String string)
	{
		if (string == null) return false;
		int len = string.length();
		
		for (int i = 0; i < len; i++) 
		{
			char c = string.charAt(i);
			if (!(c >= '0' && c <= '9'))
				return false;
		}
		
		return true;
	}
	
	/** Shuffle string's char. */
	public static String shuffle(String string)
	{		
		int len = string.length();
		char[] chars = string.toCharArray();
		
		for (int i = len - 1; i >= 0; i--) 
		{
			int num = MathUtils.random(i);
			char temp = chars[i];
			chars[i] = chars[num];
			chars[num] = temp;
		}		
		return new String(chars);		
	}
	
	/** reverse the string. */
	public static String reverse(String string) {
		int len = string.length();
		char[] chars = new char[len];
		
		for (int i = len-1; i > -1; i--)
			chars[len-i-1] = string.charAt(i);
			
		return new String(chars);
	}
	
	/** Counts how many specified chars(a list) on the string. */
	public static int countChars(String string, char... list)
	{
		int len = string.length();
		int result = 0;
		for (int i = 0; i < len; i++) 
		{
			char tem = string.charAt(i);
			for (char c : list) {
				if (c == tem) {
					result++;
					break;
				}
			}
		}
		return result;
	}
	
	/** Counts how many specified chars(a list) on the string. */
	public static int countNotChars(String string, char... list)
	{
		int len = string.length();
		int result = 0;
		for (int i = 0; i < len; i++) 
		{
			boolean bool = true;
			char tem = string.charAt(i);
			for (char c : list) {
				if (c == tem) {
					bool = false;
					break;
				}
			}
			if (bool) result++;;
		}
		return result;
	}
	
	/** Check string that has all chars on the list. */
	public static boolean checkChars(String string, char... list)
	{
		int len1 = string.length();
		int len2 = list.length;
		boolean[] bool = new boolean[len2];
		for (int i = 0; i < len1; i++) 
		{
			char c = string.charAt(i);
			for (int j = 0; j < len2; j++) 
			{
				if (list[j] == c) {
					bool[j] = true;
					break;
				}
			}
		}
		for (int i = 0; i < len2; i++) {
			if (!bool[i]) return false;
		}
		return true;
	}
	
	public static String loopChars(String string, int loops) 
	{
		int len = string.length();
		char[] chars = new char[len];
		int size = 0;
		for (int i = 0; i < len; i++)
		{
			char c = string.charAt(i);
			for (int j = 0; j < loops; j++)
			{
				chars[size] = c;
				size++;
			}
		}		
		return new String(chars);
	}
	
	public static String loopCharsRand(String string, int loops) 
	{
		int len = string.length();
		char[] chars = new char[len*loops];
		int size = 0;
		for (int i = 0; i < len; i++)
		{
			char c = string.charAt(i);
			int randNum = MathUtils.random(loops-1);
			for (int j = 0; j < randNum; j++)
			{
				chars[size] = c;
				size++;
			}
		}		
		return new String(chars, 0, size);
	}
	
	public static String createRandChars(int loops)
	{
		int len = abc.length();
		char[] chars = new char[loops];
		for (int i = 0; i < loops; i++) {
			chars[i] = abc.charAt(MathUtils.random(len-1));
		}
		return new String(chars);		
	}
	
	/** Copy the String's chars from start to end indexes. */
	public static String copyRange(String string, int start, int end) {
		if (start == end) return "";
		if (start > end) throw new IllegalArgumentException("It can't be: start("+start+") > end("+end+")");
		return new String(string.toCharArray(), start, end-start);		
	}

	/** Removes specified chars(a list) from the string. */
	public static String removeChars(String string, char... list)
	{
		int len = string.length();
		char[] chars = new char[len];
		int size = 0;
		loop : for (int i = 0; i < len; i++) 
		{
			char tem = string.charAt(i);
			for (char c : list) {
				if (c == tem) continue loop;
			}
			chars[size] = tem;
			size++;
		}
		return new String(chars, 0, size);
	}
	
	/** Removes all the letters(non-numbers) and keep the numbers. */
	public static String onlyNumbers(String string) 
	{
		int len = string.length();
		char[] chars = new char[len];
		int size = 0;
		
		for (int i = 0; i < len; i++) {
			char c = string.charAt(i);
			switch (c) {
			case '0': chars[size] = c; break;
			case '9': chars[size] = c; break;
			case '8': chars[size] = c; break;
			case '7': chars[size] = c; break;
			case '6': chars[size] = c; break;
			case '5': chars[size] = c; break;
			case '4': chars[size] = c; break;
			case '3': chars[size] = c; break;
			case '2': chars[size] = c; break;
			case '1': chars[size] = c; break;
			default: continue;
			}
			size++;
		}		
		return new String(chars, 0, size);
	}
	
	/** Removes all the numbers(non-letters) and keep the letters. */
	public static String onlyLetters(String string) 
	{
		int len = string.length();
		char[] chars = new char[len];
		int size = 0;
		
		for (int i = 0; i < len; i++) {
			char c = string.charAt(i);
			boolean isLetter = true;
			switch (c) {
			case '0': isLetter = false; break;
			case '9': isLetter = false; break;
			case '8': isLetter = false; break;
			case '7': isLetter = false; break;
			case '6': isLetter = false; break;
			case '5': isLetter = false; break;
			case '4': isLetter = false; break;
			case '3': isLetter = false; break;
			case '2': isLetter = false; break;
			case '1': isLetter = false; break;
			}
			if (isLetter) {
				chars[size] = c;
				size++;
			}
		}		
		return new String(chars, 0, size);
	}
	
	/** Toggle letters case. */
	public static String toggleCase(String string)
	{
		int len = string.length();
		char[] chars = new char[len];
		
		for (int i = 0; i < len; i++)
		{
			char c = string.charAt(i);
			if (Character.isUpperCase(c))
				chars[i] = Character.toLowerCase(c);
			else
				chars[i] = Character.toUpperCase(c);
		}
		
		return new String(chars);
	}
	
	public static String randomCase(String string, float chance)
	{
		int len = string.length();
		char[] chars = new char[len];
		for (int i = 0; i < len; i++)
		{
			char c = string.charAt(i);
			if (MathUtils.randomBoolean(chance)) {				
				if (Character.isUpperCase(c))
					chars[i] = Character.toLowerCase(c);
				else
					chars[i] = Character.toUpperCase(c);
			} else {
				chars[i] = c;
			}
		}
		return new String(chars);		
	}
	
	/** Roundly 20% faster than string.toLowwerCase(). Work better on English letters. */
	public static String fastLowerCase(String string) 
	{
		int len = string.length();
		char[] chars = new char[len];
		
		for (int i = 0; i < len; i++)
		{
			char c = string.charAt(i);
			switch (c) {
			case ' ': chars[i] = c; break;
			case ',': chars[i] = c; break;
			case '.': chars[i] = c; break;
			case '?': chars[i] = c; break;
			case '!': chars[i] = c; break;
			case 'E': chars[i] = 'e'; break;
			case 'A': chars[i] = 'a'; break;
			case 'O': chars[i] = 'o'; break;
			case 'I': chars[i] = 'i'; break;
			case 'N': chars[i] = 'n'; break;
			case 'S': chars[i] = 's'; break;
			case 'H': chars[i] = 'h'; break;
			case 'R': chars[i] = 'r'; break;
			case 'D': chars[i] = 'd'; break;
			case 'L': chars[i] = 'l'; break;
			case 'C': chars[i] = 'c'; break;
			case 'U': chars[i] = 'u'; break;
			case 'M': chars[i] = 'm'; break;
			case 'W': chars[i] = 'w'; break;
			case 'F': chars[i] = 'f'; break;
			case 'G': chars[i] = 'g'; break;
			case 'Y': chars[i] = 'y'; break;
			case 'P': chars[i] = 'p'; break;
			case 'B': chars[i] = 'b'; break;
			case 'V': chars[i] = 'v'; break;
			case 'K': chars[i] = 'k'; break;
			case 'J': chars[i] = 'j'; break;
			case 'X': chars[i] = 'x'; break;
			case 'Q': chars[i] = 'q'; break;
			case 'Z': chars[i] = 'z'; break;
			default:  chars[i] = Character.toLowerCase(c);
			}
		}		
		return new String(chars);		
	}
	
	/** Roundly 20% faster than string.toUpperCase(). Work better on English letters. */
	public static String fastUpperCase(String string) 
	{
		int len = string.length();
		char[] chars = new char[len];
		
		for (int i = 0; i < len; i++)
		{
			char c = string.charAt(i);
			switch (c) {
			case ' ': chars[i] = c; break;
			case ',': chars[i] = c; break;
			case '.': chars[i] = c; break;
			case '?': chars[i] = c; break;
			case '!': chars[i] = c; break;
			case 'e': chars[i] = 'E'; break;
			case 'a': chars[i] = 'A'; break;
			case 'o': chars[i] = 'O'; break;
			case 'i': chars[i] = 'I'; break;
			case 'n': chars[i] = 'N'; break;
			case 's': chars[i] = 'S'; break;
			case 'h': chars[i] = 'H'; break;
			case 'r': chars[i] = 'R'; break;
			case 'd': chars[i] = 'D'; break;
			case 'l': chars[i] = 'L'; break;
			case 'c': chars[i] = 'C'; break;
			case 'u': chars[i] = 'U'; break;
			case 'm': chars[i] = 'M'; break;
			case 'w': chars[i] = 'W'; break;
			case 'f': chars[i] = 'F'; break;
			case 'g': chars[i] = 'G'; break;
			case 'y': chars[i] = 'Y'; break;
			case 'p': chars[i] = 'P'; break;
			case 'b': chars[i] = 'B'; break;
			case 'v': chars[i] = 'V'; break;
			case 'k': chars[i] = 'K'; break;
			case 'j': chars[i] = 'J'; break;
			case 'x': chars[i] = 'X'; break;
			case 'q': chars[i] = 'Q'; break;
			case 'z': chars[i] = 'Z'; break;
			default:  chars[i] = Character.toUpperCase(c);
			}
		}
		return new String(chars);		
	}
}
