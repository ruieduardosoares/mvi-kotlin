# mvi-kotlin

Modern android mvi library heavily inspired by https://github.com/sockeqwe/mosby

### Why was this library created, if the mosby library works fine?
Currently, one of the disadvantages that we detected on the mosby library is that, for those who dig the internals, it uses a mechanism for Activity, Fragment, ViewGroup to retain the
presenter when the android component goes through a recreation process. This mechanism was explicitly created by the developer.

Given the advancements of the **AndroidX**, we believe we can take advantage of a **ViewModel**, which survives through the recreation process of the given android component to keep the presenter instance available to recover when the component is recreated, at the end doing the same thing as the mosby internal mechanism does.

See this simple implementation we use to store and recover the presenter in [MviContainerPresenterMemento](https://github.com/ruieduardosoares/mvi-kotlin/blob/f66a2812b28f4ae56a5ea6672741f25fdfa1c8e0/app/src/main/kotlin/io/github/ruieduardosoares/android/mvi/kotlin/containers/MviContainerPresenterMemento.kt)

Advantages of this decision are:
- **Less code** plus mantaining the same behavior as **mosby** library.
- Library size decreases for your project

## How do i import this library into my project?

To import the library into you project you need to configure the following in your **app.gradle** file

*In app/build.gradle*
```
repositories {  
  // ...
  maven { url "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/" }  
}
```
Then on the same file you need to import the latest version of the library

*In app/build.gradle*
```
dependencies {  
  // ...
  implementation "io.github.ruieduardosoares:android-mvi-kotlin:x.x.x"
}
```

## License
```
MIT License  
  
Copyright (c) [2021] [Rui Eduardo Soares]  
  
Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software, and to permit persons to whom the Software is  
furnished to do so, subject to the following conditions:  
  
The above copyright notice and this permission notice shall be included in all  
copies or substantial portions of the Software.  
  
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  
SOFTWARE.  
```
