package com.bryankeiren.fjord;

public class Util 
{
	public static float Lerp( float _From, float _To, float _F )
	{
		return _From + (_To - _From) * _F;
	}
	
	public static double Lerp( double _From, double _To, double _F )
	{
		return _From + (_To - _From) * _F;
	}
	
	public static float LerpFactor( float _Min, float _Max, float _Value ) 
	{
		return (_Value - _Min) / (_Max - _Min);
	}
}
