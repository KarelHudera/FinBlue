import SwiftUI
import shared

struct ContentView: View {
    @State private var viewModel: FeedViewModel = KoinKt.getFeedViewModel()
    
    var body: some View {
        VStack {
            Observing(viewModel.stateFlow) { uiState in
                switch uiState {
                case is MovieListUiState.Loading:
                    ProgressView()
                case let error as MovieListUiState.Error:
                    RetryView(message: error.message, retry: viewModel.refresh)
                case let success as MovieListUiState.Success:
                    List {
                        ForEach(Array(success.result.enumerated()), id: \.1) { index, collection in
                            FBRow(categoryName: collection.sortTypeStringDesc.resolve(), items: collection.feeds,
                                    index: index)
                        }
                    }.listStyle(.inset)
                default:
                    fatalError("Unhandled state: \(uiState)")
                }
            }
            
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
