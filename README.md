# FirebaseTest

Firebase test project which connects to the server, retrieves messages list form *messages* endpoint and shows messages, 
filtering the words which are retrieved from *words* endpoint.

# Class overview

DatabaseValue - class to store values from server.

ReplacementUnit - class to store the word to replace and the original lenght of that word to replace 
it with the correct number of * symbols.

WorldFilterHelper - this class contains possible ways that user can change original word to avoid replacement

SenitiseManager - class which manages the replacement process.
- List replacementSnowballs - contains list of Strings which looks like: *, **, *** etc. 
Will have as many element as lenght of the longest word to replace. This was done to reduce string operations. Some kind of cache.
This list is being updated in method updateReplacementSnowballs() if needed.
- List replacementUnits - contains original words from backend and all the possible replacements
- List replacementMethods - contains list of the methods with different replacements that user can make. 
List of Methods are being retrieved from WordFilterHelper class using the reflection. 
This is done to ensure that new replacement method invented by users can quckly be added 
to WordFilterHelper and no other changes needs to be done to apply a new rule.
Any new method in WordFilterHelper which have a *Senitise* in name and a public acces will be added to list.
Check the method collectMethods() to see how it's done.

ChatActivity - this Activity controls retrieving data from backend. 
- On the start of activity method getReplaceWords() will be called to retieve 
first *words* data from backend and then retrieving the *messages* data. Both this methods are used to retrieve data only onse by using
addListenerForSingleValueEvent() method.
- After initial data was retrieved method setupListeners() is called using the addValueEventListener() fot both *messages* and *words* 
to ensure data being updated propertly if something changes on server side.
- The button at the bottom to the right is needed to refresh data manually by calling the getReplaceWords().
- If the Activity method onStop() is called - listeners are being removed in removeListener() method.

# Additional libraries

'net.bohush.geometricprogressview:geometricprogressview:1.0.0' - for this awesome loading animation.
'com.android.support:appcompat-v7:25.3.0' - for the floating action button.
