＃Okhttp
加依赖：
1.将其添加到存储库末尾的root build.gradle中：
      allprojects {
          repositories {
            ...
            maven { url 'https://jitpack.io' }
          }
        }
2.添加依赖项：
      dependencies {
	        implementation 'com.github.WoShiDiaoSi:Okhttp:Tag'
	}
