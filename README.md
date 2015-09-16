# Popular-Movies
The complete functionality of the app can be divided into two stages:-

### Stage-1
In this stage we will build the core experience of this movie app

This app upon launch will:

* Present the user with a grid arrangement of movie posters upon launch.
* Allow the user to change sort order via a setting: 
    - The sort order can be by most popular or by highest-rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as: 
    - movie title
    - movie poster
    - release date
    - plot synopsis (called overview in the api)
    - user rating (called vote average in the api)
    - genre of a movie
    - running time
    
#### Setup Library Configuration for Stage-1
To fetch images & load them into views we have used a library called ***Glide***

You can download the Glide by going through the page in github [Visit GitHub!](https://github.com/bumptech/glide)

The API which is used in making this app is **themoviedb.org**


##### Note on resolving the poster path of a movie
The API response provides a relative path to a movie poster upon request for a movie

For example, the poster path return for Interstellar is “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”

We need to append a base path ahead of this relative path to build the complete url you will need to fetch the image using Glide.

It’s constructed using 3 parts:
  
  1. The base URL will look like: http://image.tmdb.org/t/p/
  
  2. Then you will need a ‘size’, which will be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original". But in this app we have used “w185”.
  
  3. And finally the poster path returned by the query, in this case “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”

Combining these three parts gives us a final url of http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg 

###How to fetch movies from the API

* To fetch the movies, we will use the api from themoviedb.org
    - If you don't have an account in themoviedb.org API, you have to create a one to request for an API key
    - Once you obtain your key, append it to your HTTP request as a URL parameter like so:
        * http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=YOUR API KEY
    - Extract the movie id from this request as we are going to need this in subsequent request
    
### Stage-2
In this stage we’ll add additional functionality to the app we built in Stage 1.

We will add additional details to the detail page of a movie:
* We'll allow the user to view & play trailers of a movie
* We'll allow the user to read the reviews of a selected movie
* To fetch the reviews make a request to http://api.themoviedb.org/3/movie/{id}/reviews?api_key=YOUR_API_KEY 
* To fetch the trailers make a request to http://api.themoviedb.org/3/movie/{id}/videos?api_key=YOUR_API_KEY 
