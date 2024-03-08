#include <iostream>
  
extern "C" {
    void myFunction() {
            std::cout << "Hello from C++ function!" << std::endl;
         }
}
