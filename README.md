# Proiect GlobalWaves  - Etapa 2

This is the second phase of the **GlobalWaves** project. The project is a music streaming
service, similar to Spotify. The project is written in Java and uses the JSON.simple library to
parse the input files.


## Skel Structure

* app/
    * audio/
        * Collections/
            * Album
            * AlbumOutput
            * AudioCollection
            * Playlist
            * PlaylistOutput
            * Podcast
        * Files/
            * AudioFile
            * Episode
            * Song
        * LibraryEntry
    * player/
        * Player
        * PlayerSource
        * PlayerStats
        * PodcastBookmark
    * searchbar/
        * Filters
        * FilterUtils
        * SearchBar
    * user/
        * Artist
        * Host
        * User
        * UserEntry
    * utils/
        * Enums
    * Admin
    * CommandRunner
  
* checker/ - checker files
    * Checker
    * CheckerConstants
    * CheckStyle
* fileio/ - contains classes used to read data from the json files
    * AnnouncementInput
    * CommandInput
    * EpisodeInput
    * EventInput
    * FiltersInput
    * Input
    * LibraryInput
    * MerchInput
    * PodcastInput
    * SongInput
    * UserInput
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point
    to your implementation in it. Run Main to test your implementation from the IDE or
    from command line.
      * Test - run the main method from Test class with the name of the input file from the
    command line and the result will be written
      to the out.txt file. Thus, you can compare this result with ref.

## Description

For the pages system, I created an enum called ***CurrentPage***, which contains all the pages
that can be accessed(HomePage, HostPage, ArtistPage, LikedContentPage etc.). Any user is set by
default to the HomePage, and the user can navigate to other pages by using the **changePage**
method from the ***Admin*** class. For the **printCurrentPage** command I chose to make a method
for each page, because I thought it would be easier to read and understand the code. I then
created the ***printCurrentPage*** method, which calls one of the methods for each page, depending
on the current page.

In the ***SearchBar*** class I updated the **search** method to also include the 'album' case
and renamed it to **searchLibrary**, because I thought it would be more appropriate to make a
separate method for searching the library than the one for searching the users, for which I added
the **searchUser** method. I did the same thing for **select**.

I created the ***UserEntry*** class similar to the ***LibraryEntry*** one, which contains the
username, age, city and the user type. The classes ***Artist***, ***Host*** and ***User*** extend
the ***UserEntry*** one, and contain the information specific to each type of user. The **addUser**
and **deleteUser** methods from the ***Admin*** class first treat the error cases using additional
methods such as **isUser**, **isArtist** and **isHost**, and then add or delete the user and update
everything accordingly. For **deleteUser** I treated all the cases where the user can't be deleted.

The **addAlbum** and **addPodcast** methods from the ***Admin*** class are very similar, treating
all the error cases first and then adding the abum for the artist or the podcast for the host.
The same thing applies to **removeAlbum** and **removePodcast**.

In ***Artist***, I chose to create the methods for adding and removing an event, as well as the
**addMerch** method. For **addEvent** I defined in ***CheckerConstants*** some constants for
parsing the date. I also created the ***EventInput*** and ***MerchInput*** classes in this scope.

The **addAnnouncement** and **removeAnnouncement** methods are in the ***Host*** class,
and they are very similar to the ones for adding and removing an event, so I also created
the ***AnnouncementInput*** class in this scope.

For the 'switchConnectionStatus' command I created the **ConnectionStatus** enum, which contains
the two possible states of the connection. The **switchConnectionStatus** method from the
***User*** class changes the connection status from online to offline and vice-versa, and it is
called in the **switchUserStatus** method, which also treats the error cases and returns a success
message if everything went well.

The ***CommandRunner*** class creates ObjectNodes for each command, which will be later called
in the ***Main*** class.

