## MoneytreeTest

### High level architecture
UI (activity & fragments)
Viewmodel (Livedata for UI)
Repository (Local data source & remote data store)
SQLite can be added as needed

Could have used data binding to update UI elements (progress, buttons)


### UI
ViewModel: fragment controller. Prepares and stores data.
Fragment: for accounts and transactions screens. UI updates happen here. Requests data from viewmodel (getAccounts, getAccountData)
Adapter: for recyclerview
Dashboard recycler: has cards as items with account rows inserted by code in bind()
Transactions recycler: has custome type items (header & transactions rows). Adapter inflated item based on type

### Data layer
Repository: Abstracts local/remote/cache. Managed getting data from remote, local or cache.
Localdata: gets data from json files.
Remotedata: stub for now. api can be added here

### Testing
Unit tests:
viewmodels- inject fake repository to test behavior
defaultrepository- create repository with fake data source to test
Coroutine management: pause and resume to allow testing

Instrumentation:
Navigation testing
Fragment level testing for views
Livedata: get livedata value to test loadin/success/error


### Others
Accessability:
Used Accessability scanner
Fixed: descriptions, touch sizes, color contrast
Could not fix back button label warning

Warnings: (not fixed)
Background overdraw- Can be fixed by fixing theme properly
@Parcelize: IDE does not recognise @Parcelize yet.
kotlin property name warnings
Spelling Typo
