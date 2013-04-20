/*******************************************
*
* Header file for menu stuff.
*
*******************************************/
#ifndef __MENU_H
#define __MENU_H

#include <pololu/orangutan.h>  

#define MENU "\rMenu: {LRFBS}: "
#define MENU_LENGTH 16 

/* This is a customization of the serial2 example from the Pololu library examples. (ACL)
 *
 * serial2: for the Orangutan SVP and Orangutan X2 controllers.
 *
 * This example listens for serial bytes transmitted via USB to the controller's
 * virtual COM port (USB_COMM).  Whenever it receives a byte, it performs a
 * custom action.  
 *
 * This example will not work on the Orangutan LV, SV, Baby Orangutan, or 3pi robot.
 * 
 * http://www.pololu.com/docs/0J20 
 * http://www.pololu.com  
 * http://forum.pololu.com  
 */   

// wait_for_sending_to_finish:  Waits for the bytes in the send buffer to
// finish transmitting on USB_COMM.  We must call this before modifying
// send_buffer or trying to send more bytes, because otherwise we could
// corrupt an existing transmission.
void wait_for_sending_to_finish();

// process_received_byte: Parses a menu command (series of keystrokes) that 
// has been received on USB_COMM and processes it accordingly.
// The menu command is buffered in check_for_new_bytes_received (which calls this function).
void process_received_string(const char*);


// If there are received bytes to process, this function loops through the receive_buffer
// accumulating new bytes (keystrokes) in another buffer for processing.
void check_for_new_bytes_received();

// This initializes serial communication through the USB.
void init_menu();

// A generic function for whenever you want to print to your serial comm window.
// Provide a string and the length of that string. My serial comm likes "\r\n" at 
// the end of each string (be sure to include in length) for proper linefeed.
void print_usb(char*,int);

#endif //__MENU_H
