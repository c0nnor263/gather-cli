# gather-cli
### Setup
##### build.gradle(Project)
```groovy
buildscript {
    repositories {
        mavenCentral()
    }
}
```

##### build.gradle(Module)
```groovy

android{
    dependencies {
        implementation 'io.github.c0nnor263:gather-cli:1.0.0'
    }
}

```

### How to use:

```kotlin

    lateinit var gathClient:GathClient

    ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gathClient =
            GathClient(lifecycleOwner = this, webView = binding.root)
                .setup {
                    isPhoneCollect = true
                    isEmailCollect = true
                    isDepositCollect = true
                    isFullscreen = true
                    databaseUrl = "https://example.firebaseio.com/"     // optional
                }
                .start()
    }


    private fun createWebViewClient():WebViewClient = object:WebViewClient(){
        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            gathClient.evaluateJavascript(view)
        }
    }
    
```
