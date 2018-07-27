## How to create a release?

We're using the [gradle release plugin](https://github.com/researchgate/gradle-release) which helps us to automatically create a new release, commit the new versions and tag the release. 
 
 This can simple be done by the following gradle command: 

```
./gradlew release 
```

The plugin will ask for the new *release version* and the subsequent *snapshot version*. 

After the tag is pushed, a new build will be triggered on Travis. Since this build is triggered by a *Git Tag* the build will automatically create a new release on Github and attach the demo.zip file as artifact to the release. 

For further information on the release upload to github, take a look at the `.travis` file in the root of the repository. (deploy section) 

The [Travis](https://docs.travis-ci.com/user/deployment/releases/) documentation  is also very helpful. 
