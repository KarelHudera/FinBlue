//
//  FBRow.swift
//  iosApp
//
//  Created by Karel Hudera on 3/14/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct FBRow: View {
    var categoryName: String
    var items: [Movie]
    var index: Int

    var body: some View {
        VStack(alignment: .leading) {
            Text(categoryName)
                .font(.headline)
                .padding(.leading, 15)
                .padding(.top, 5)
                .padding(.bottom, 10)

            ScrollView(.horizontal, showsIndicators: false) {
                HStack(alignment: .top, spacing: 0) {
                    ForEach(items, id: \.self.id) { movie in
                        FBItem(movie: movie, index: index)
                    }
                }
            }
            .frame(height: Constants.frameSize)
        }
    }
    
    private struct Constants {
        static let frameSize: Double = 185
    }
}


#Preview {
    let movie = Movie(id: 1, overview: "", releaseDate: nil, posterPath: nil, backdropPath: nil, name: "name", voteAverage: 1, voteCount: 1)
    return FBRow(
        categoryName: "Trending",
        items: [movie],
        index: 1
    )
}
