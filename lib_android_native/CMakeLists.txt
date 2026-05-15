cmake_minimum_required(VERSION 3.22.1)

project(muxxhash)

add_library(muxxhash SHARED
        src/main/cpp/xxhash.c
        src/main/cpp/xxhash_jni.c)

target_include_directories(muxxhash PRIVATE src/main/cpp)

target_compile_options(muxxhash PRIVATE -O3)

find_library(log-lib log)
target_link_libraries(muxxhash ${log-lib})
