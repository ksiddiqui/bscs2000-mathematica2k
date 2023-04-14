package org.ksiddiqui.bscs.mathematica.maths;

public class Complex
{

        private double real;
        private double imaginary;
        
    /******************************************************************
    *           CONSTRUCTURES
    ******************************************************************/
        
        
        Complex()
          {
           real = imaginary = 0;
          }
          
        Complex(double a, double b)
          {
           real = a;
           imaginary = b;
          }
          
        Complex(String a, String b)
          {
           real = Double.parseDouble(a);
           imaginary = Double.parseDouble(b);           
          }  

    /******************************************************************
    *           SETTING & GETTING METHODS
    ******************************************************************/
    
        public double getReal()              { return real; }
        public double getImaginary()         { return imaginary; }
          
        public String getRealStr()           { return String.valueOf(real);}
        public String getImaginaryStr()      { return String.valueOf(imaginary); }        
          
        public void setReal(double a)        { real = a; }
        public void setReal(String a)        { real = Double.parseDouble(a); }

        public void setImaginary(double a)   { imaginary = a; }
        public void setImaginary(String a)   { imaginary = Double.parseDouble(a); }

    /******************************************************************
    *           ARITHEMATIC OPERATION METHODS
    ******************************************************************/

        public Complex add(double num)
          { return new Complex(real+num, imaginary); } 
        public Complex add(Complex c)
          { return new Complex(real+c.real, imaginary+c.imaginary); } 


        public Complex sub(double num)
          { return new Complex(real-num, imaginary); } 
        public Complex sub(Complex c)
          { return new Complex(real-c.real, imaginary-c.imaginary); } 

          
        public Complex mul(double num)
          { return new Complex(real*num, imaginary*num); } 
        public Complex mul(Complex c)
          { return new Complex((real*c.real-imaginary*c.imaginary), (real*c.imaginary+imaginary*c.real)); } 

  
        public Complex div(double num)
          { return new Complex(real/num, imaginary/num); } 
        public Complex div(Complex c)
          {
           Complex tmp = this.mul(new Complex(c.real, -c.imaginary)); 
           tmp = tmp.div((c.real*c.real+c.imaginary*c.imaginary));           
           return tmp;
          } 

        public Complex divR(double num)
          { 
           Complex tmp = new Complex(real, -imaginary);
           tmp.mul(num); 
           tmp = tmp.div((real*real+imaginary*imaginary));           
           return tmp;           
          } 
        public Complex divR(Complex c)
          {
           Complex tmp = new Complex(real, -imaginary);
           tmp.mul(c); 
           tmp = tmp.div((real*real+imaginary*imaginary));           
           return tmp;
          } 


        public Complex sqr()
          {
           return new Complex((real*real-imaginary*imaginary),(2*real*imaginary));
          }
          
        static public Complex sqrt(double num)
          {
           boolean isComplex=false;
           
           if (num<0) isComplex=true;
           num = (num<0) ? -num:num ;           
           double sqrt = Math.sqrt(num);
           
           if (isComplex)  return new Complex(0, sqrt); 
           return new Complex(sqrt, 0); 
          }  

}
