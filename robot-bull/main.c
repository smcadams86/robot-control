#include "main.h"

void print_info(double e1, int btn_pressed);

int main() {

  // Used to print to serial comm window
  char tempBuffer[32];
  int length = 0;
  
  // Initialization here.
  encoders_init(IO_D2, IO_D3, IO_D4, IO_D5);
  lcd_init_printf();  // required if we want to use printf() for LCD printing
  init_menu();  // this is initialization of serial comm through USB
  clear();  // clear the LCD

  current_command = STOP;

  while (1) {
    serial_check();
    check_for_new_bytes_received();

    switch(current_command) {
      case LEFT:
        set_motors(0, -WHEEL_SPEED);
        set_motors(1, WHEEL_SPEED);
        break;
      case RIGHT:
        set_motors(0, WHEEL_SPEED);
        set_motors(1, -WHEEL_SPEED);
        break;
      case FORWARD:
        set_motors(0, WHEEL_SPEED);
        set_motors(1, WHEEL_SPEED);
        break;
      case BACKWARD:
        set_motors(0, -WHEEL_SPEED);
        set_motors(1, -WHEEL_SPEED);
        break;
      case STOP:
      default:
        set_motors(0, 0);
        set_motors(1, 0);
        break;
    }
    lcd_goto_xy(0,0);
    printf("Command : %d", current_command); 
    delay_ms(100);
  }
}

