# CMakeList.txt: CarRecgnize 的 CMake 项目，在此处包括源代码并定义
# 项目特定的逻辑。
#
cmake_minimum_required (VERSION 3.8)

#引入头文件
# home
#include_directories("D:/programtools/opencv/opencv/build/include")
# office 
include_directories("D:/programs/opencv/opencv/build/include")

# 引入库文件
# home
#link_directories("D:/programtools/opencv/opencv/build/x64/vc15/lib")
# office
link_directories("D:/programs/opencv/opencv/build/x64/vc15/lib")

# 将源代码添加到此项目的可执行文件。
add_executable (CarRecgnize "CarRecgnize.cpp" "CarPlateRecgnize.cpp" "CarSobelPlateLocation.cpp" "CarColorPlateLocation.cpp" "BaseCarPlateLocation.cpp")

# TODO: 如有需要，请添加测试并安装目标。
# 链接opencv_world343d动态链接库
target_link_libraries(CarRecgnize opencv_world343d)
