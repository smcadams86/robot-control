#include <pololu/orangutan.h>

#define ONE_ROTATION 63
#define WHEEL_SPEED 200

#define LEFT 0
#define RIGHT 1
#define FORWARD 2
#define BACKWARD 3
#define STOP 4

// Global variables
int current_command;
