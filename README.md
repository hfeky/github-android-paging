
# GitHubPaging

A sample app that demonstrates the usage of pagination with a one-to-many relation in Room, with data fetched from GitHub API.

## App Demo

App demo uses `FetchingStrategy.NETWORK_FIRST` to fetch the data. This means the data will always be fetched from the network first then get saved, and if it failed to fetch the data from the network, it will be fetched from the database.

Watch on YouTube:

[![App Demo](https://img.youtube.com/vi/-MynWx-N6UE/maxresdefault.jpg)](https://www.youtube.com/watch?v=-MynWx-N6UE)

## Repo Branches

**master:** Pagination is implemented, using Android Jetpack Paging library that is implemented by Google, with data fetched daily by `WorkManager`.

**custom-paging:** Pagination is implemented from scratch, with newly created base paging classes that adds much more flexibility to customize how data is paginated, with data fetched page by page.

## Jetpack Paging Library vs. Custom Paging

| Point of Comparison | Jetpack Paging Library | Custom Paging |
|--|--|--|
| Paginating by page | ✔️ | ✔️ |
| Paginating by item | ✔️ | ✔️ |
| Loading from Network only | ✔️ | ✔️ |
| Loading from Database only | ✔️ | ✔️ |
| Loading from Database + Network | ✔️* | ✔️ |
| Adding boundary callback | ✔️ | - |
| Adding retry callback | - | ✔️ |
| Setting prefetch distance | ✔️ | ✔️ |
| Setting maximum items size in list | ✔️ | - |
| Setting different fetching strategies | - | ✔️ |
| Handles different pagination states easily | - | ✔️ |
| Supports creating placeholders | ✔️ | - |
| Supports caching mechanism | - | ✔️ |
| Supports modifying data size returned from Room | - | ✔️ |
| Supports observing data changes | - | ✔️ |
| Supports invalidating data source | ✔️ | ✔️ |
| Supports Coroutines | ✔️ | - |
| Supports RxJava| ✔️ | ✔️ |

\* Concerning loading from database and network, **Jetpack Paging library** has limited capability; it only supports loading from database and when one reaches the beginning or the end of the list, if more items need to be fetched, they can be fetched through network and only by giving the first or last item in the list, respectively, through `PagedList.BoundaryCallback`, while **Custom Paging** supports loading from any resource anytime in any page by setting a `FetchingStrategy` and overriding `shouldFetchFromNetwork` method.

## Data Source Equivalence

| Jetpack Paging Library | Custom Paging |
|--|--|
| `PageKeyedDataSource` | `PagedNetworkResource` |
| `ItemKeyedDataSource` | `IndexedNetworkResource` |
| `PositionalDataSource` | `PagedDatabaseResource` |

## Data Source Types

| Data Source | Description |
|--|--|
| `PagedDataSource` | Paginate by page, either from database or network. |
| `IndexedDataSource` | Paginate by item, either from database or network. |
| `BasicDataSource` | Helper data source to fetch data either from database or network but without pagination. |
| `PagedDatabaseResource` | Paginate by page from the database only. |
| `IndexedDatabaseResource` | Paginate by item from the database only. |
| `PagedNetworkResource` | Paginate by page from the network only. |
| `IndexedNetworkResource` | Paginate by item from the network only. |

## Fetching Strategy Types

Fetching Strategy can be used in `PagedDataSource`, `IndexedDataSource`, or `BasicDataSource`.

| Fetching Strategy | Description |
|--|--|
| `NETWORK_FIRST` | Fetch data from the network first; if it fails, fetch from the database. |
| `CACHE_FIRST` | Fetch data from the database first; if no data is found or the data is outdated and needs to be refreshed, fetch from the network. |
| `NETWORK_ONLY` | Fetch data from the network only. |
| `CACHE_ONLY` | Fetch data from the database only. |

## Adding Caching Layer

Adding a caching layer is optional. You can implement `CachingLayer` interface in `PagedDataSource`, `NetworkDataSource`, `PagedNetworkResource`, or `IndexedNetworkResource` to save data in the database automatically whenever data is fetched from the network successfully.

## Contribution

Of course there is still a lot of room for improvement, mainly adding support for Kotlin Coroutines as well. Any contribution is indeed welcome.
