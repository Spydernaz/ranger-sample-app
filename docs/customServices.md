# Pulling Tags for a custom service in Ranger #

The premise of this document is to document how to extend Apache Ranger to add a new custom service and how to use Tag Sync to pull these tags. I will be using the Code found in this repo to describe how we deployed a custom HTTPService to Ranger and pulled tags on the HTTP_PATH created in atlas.

## Requirements ##

- Apache Atlas
- Apache Ranger (with TagSync configured to use Atlas for a tag source)
- Code in this repo (either compiled JARs or maven to compile the source code)

## Steps ##

### 1. Create your application ###

Our webapp is created in `./../source-code/webapp` and is a java webapp that has a few test routes:

- /hello
- /hello/{name}
- /application.wadl

In the `/hello` route, you are able to see a method called `authoriser.authorise`. This is a method in the `RangerWebAuthorizer` class that checks if a user is allowed to access a particular resource using a particular action based on defined policies in Ranger. The response is a boolean response, either allowing the user access or denying them.

you can see how we deployed a custom

While this approach supports resource based policies, Ranger does not know what a http_path is in Atlas and therefore it cannot apply tag based policies to thiese resources.

### 2. Extending Ranger TagSync ###

To start, we need to register our new resource types in Apache Atlas. The documentation to do this can be found at `./../docker/atlas/README.md`. Once these new types have been created, we can deploy our TagSync Extension. To do this, we need to add the `.jar` file to the ranger tagsync `/dist` folder. This can usually be found at `/usr/hdp/current/ranger-tagsync/dist`.

Once the file is copied we need to register it in the TagSync properties. If you are using ambari to manage the cluster, ensure that the changes are made through ambari. Below is the property that needs to be appended to the tagsync configuration:

```sh
ranger.tagsync.atlas.custom.resource.mappers=com.spyder.security.metadata.AtlasHTTPServiceResourceMapper
```

Now restart TagSync and you should see the tags being downloaded for the new service.
