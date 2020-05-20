# Sukshi Camera Demo
A face detect capture library

## Setup
### Gradle :
##### Step 1 :
Add the JitPack repository to your build file
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
##### Step 2 :
Add the dependency

```java
dependencies {
	       implementation 'com.github.imanshul:SukshiCamerDemo:1.0.0'
	}
  
  ## Usage
  Add the camera and write external storage permission in Manifest. Ask user for runtime permission before executing the below code. Start the Camera activity using startActivityResult providing the request code. 
  
  ```java
  int REQUEST_CODE = 123;
  
 startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), REQUEST_CODE);
  ```
and at the end overide the onActivityResult to get results
  ```java
     @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                String filePath = data.getStringExtra(CameraActivity.FILE_PATH_KEY);
                Log.i("file_path", "Path = " + filePath);
            }
        }
    }

  ```
  
## LICENSE
```
MIT License

Copyright (c) 2019 Anshul Thakur

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions 
of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
