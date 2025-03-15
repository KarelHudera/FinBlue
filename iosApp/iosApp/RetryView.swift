import SwiftUI
import shared

struct RetryView: View {
    let message: String
    let retry: () -> Void
    
    var body: some View {
        VStack(spacing: 20) {
            Text(message)
            Button(action: {
                retry()
            }, label: {
                Text(getRetryText())
            })
        }
    }
}
