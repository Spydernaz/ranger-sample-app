# Concept #

Extend Apache Ranger to support Policy Management of Web Services off the Hadoop Ecosystem.

## What is Apache Ranger ##

[Apache Ranger](http://ranger.apache.org/) is a framework to enable, monitor and manage comprehensive data security across the Hadoop platform. It provides a single point to manage the authorization in the environment. This centralisation is important as disparate management policies can lead to an ambiguous security landscape and mismanagement of access policies.

>Apache Ranger has the following goals:
>
> - Centralized security administration to manage all security related tasks in a central UI or using REST APIs. 
> - Fine grained authorization to do a specific action and/or operation with Hadoop component/tool and managed through a central administration tool
> - Standardize authorization method across all Hadoop components.
> - Enhanced support for different authorization methods - Role based access control, attribute based access control etc.
> - Centralize auditing of user access and administrative actions (security related) within all the components of Hadoop.

## Repository Goals ##

The aim of this repository is to combine 2 facts:

- There is significant benefit in centralising security administration
- There may be components in a Data Platform that do not exist in the Hadoop Ecosystem

With these 2 points realised, this repo is starting with a simple extension to prove value. The initial goal is to extend Ranger to support HTTP Service Endpoints (i.e. a WebApp or API). This could be used to manage microservices that support the Data Platform, expose ML / AI models or provide an interface to other data stores.

After this service has been built out, I am looking to write a security extension to Neo4J that would enable Apache Ranger to provide fine-grain access control to resources, essentially providing the framework for ABAC functionality in a graph database.

# Running the Application #

What is the point of sharing a repo if nobody else can test or use the work you are sharing.

While the documentation is still quite a bit rusty I hope that this page will allow you to follow along while there are no packages. This should allow local testing of Ranger as well as any of the extensions I have built in the repo.

Most of the Docker components will have an image in [docker hub](https://hub.docker.com/u/spydernaz) that I will attempt to keep updated however if there are issues please raise an issue in the repo

## Guides ##

There are 2 Guides, building using Docker/Docker Compose & building from source (currently a little bit involved)
Both of these guides assume you are on a Unix system and can run a `curl` command. Both (but more so the second) expects you to be comfortable with the CLI and have an IDE (I recommend [VS Code](
https://code.visualstudio.com/))

### Docker Example ###

#### Prerequisites ####

To follow along and test the features in this repo you will need the following:

- Docker Installed with docker-compose enabled (I think this is always by default now?)
- Git
- Active Internet connection (for pulling images)

#### Steps ####

##### Part 1. - Start the Services #####

1. Clone the Repo `git clone https://github.com/Spydernaz/ranger-sample-app.git`
2. Enter the repository `cd ranger-sample-app`
3. Run the docker-compose `docker-compose up --build`

##### Part 2. - Create a Test Policy in Ranger and Test #####

> The current demo has a hardcoded policy implemented for the `/hello` route. 
> To test, run the following command:
>
> ```sh
> curl localhost:8080/hello
> ```
>
> You should receive the following message `Unauthorized: 403!` but after the following steps, you should be able to configure a policy such that the response will be `Hello World!`

1. Go to [ranger-admin](http://localhost:6080)
2. Login to the UI `admin`:`passw0rd`
3. Create the new HTTPService interface `curl -u admin:passw0rd -X POST -H "Accept: application/json" -H "Content-Type: application/json" --data @./docker/http_plugin/service_def_http.json http://localhost:6080/service/plugins/definitions`
4. Add a new httpservice instance (click the plus on the ranger http service)
5. If you are using the default application, then the name should be `helloworldapp`
6. Enter a URL (won't matter at the moment, open issue #9)
7. Add the service
8. Add a test policy by clicking the helloworldapp service > Add a Policy
9. Enter a Policy Name for the policy and use the HTTP Path **/hello**
10. Configure the user to be **Admin** and have the permission **get** and save the policy
11. Run `curl localhost:8080/hello`. You should get the response `Hello World!`

### Compile form Source - Example ###

// TODO
