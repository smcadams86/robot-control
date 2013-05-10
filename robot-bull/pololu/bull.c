#include <pololu/orangutan.h>  
  
/*  
 * digital1: for the Orangutan LV, SV, SVP, X2, Baby-O and 3pi robot.
 *  
 * This example uses the OrangutanDigital functions to read a digital 
 * input and set a digital output.  It takes a reading on pin PC1, and 
 * provides feedback about the reading on pin PD1 (the red LED pin). 
 * If you connect a wire between PC1 and ground, you should see the 
 * red LED change state. 
 *  
 * http://www.pololu.com/docs/0J20 
 * http://www.pololu.com  
 * http://forum.pololu.com  
 */

 #define LEFT_MOTOR_ON IO_C0
 #define RIGHT_MOTOR_ON IO_C1
 #define LEFT_MOTOR_REVERSE IO_B3
 #define RIGHT_MOTOR_REVERSE IO_B4
 #define MOTOR_SPEED 100;

char is_left_motor_on() {
    return is_digital_input_high(LEFT_MOTOR_ON);
}

char is_left_motor_reversed() {
    return is_digital_input_high(LEFT_MOTOR_REVERSE);
}

char is_right_motor_on() {
    return is_digital_input_high(RIGHT_MOTOR_ON);
}

char is_right_motor_reversed() {
    return is_digital_input_high(RIGHT_MOTOR_REVERSE);
}
  
int main()  
{  
    set_digital_input(LEFT_MOTOR_ON, PULL_UP_ENABLED);  
    set_digital_input(RIGHT_MOTOR_ON, PULL_UP_ENABLED);
    set_digital_input(LEFT_MOTOR_REVERSE, PULL_UP_ENABLED);
    set_digital_input(RIGHT_MOTOR_REVERSE, PULL_UP_ENABLED);

    int left_motor = 0;
    int right_motor = 0;
  
    while(1)  
    {   clear();

        //Left Motor
        lcd_goto_xy(0,0);
        if(is_left_motor_on())     // Take digital reading of PC1.  
        {
            if(is_left_motor_reversed())
            {
                left_motor = -MOTOR_SPEED;
                print("Left Motor Reversed");
            }
            else
            {
                left_motor = +MOTOR_SPEED;
                print("Left Motor Forward");
            }   
        }  
        else  
        {
            left_motor = 0;
            print("Left Motor Stop");  
        }

        //Right Motor
        lcd_goto_xy(0,1);
        if(is_right_motor_on())     // Take digital reading of PC1.  
        {
            if(is_right_motor_reversed())
            {
                right_motor = MOTOR_SPEED;
                print("Right Motor Reversed");
            }
            else
            {
                right_motor = -MOTOR_SPEED;
                print("Right Motor Forward");
            }   
        }  
        else  
        {
            right_motor = 0;
            print("Right Motor Stop");
        }
        set_motors(right_motor, left_motor);
        delay_ms(200);  
    }  
}