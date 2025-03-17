//
//  FBItem.swift
//  iosApp
//
//  Created by Karel Hudera on 3/14/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared
import SDWebImageSwiftUI

struct FBItem: View {
    var movie: Movie
    var index: Int
    
    var body: some View {
        if(index % 3 == 0) {
            item(width: Constants.frameWidthLarge, imageUrl: movie.backdropPath)
        } else {
            item(width: Constants.frameWidthMedium, imageUrl: movie.posterPath)
        }
    }
    
    func item(width: Double, imageUrl: String?) -> some View {
        let shape = RoundedRectangle(cornerRadius: Constants.cornerRadius)
        let placeHolder = shape.foregroundColor(.secondary)
            .frame(width: width, height: Constants.frameHeight)
        return VStack(alignment: .leading) {
            if let posterUrl = imageUrl {
                WebImage(url: URL(string: String(format: posterUrl))) { image in
                    image
                        .resizable()
                        .frame(width: width, height: Constants.frameHeight)
                        .clipShape(shape)
                } placeholder: {
                    placeHolder
                }
                Text(movie.name)
                    .font(.caption)
                    .truncationMode(.tail)
                    .frame(maxWidth: width)
            } else {
                placeHolder
            }
        }
        .padding(.leading, 15)
    }
    
    private struct Constants {
        static let cornerRadius: Double = 20
        static let frameWidthLarge: Double = 225
        static let frameWidthMedium: Double = 125
        static let frameHeight: Double = 175
    }
}


#Preview {
    let movie = Movie(id: 1, overview: "", releaseDate: nil, posterPath: nil, backdropPath: nil, name: "name", voteAverage: 1, voteCount: 1)
    return FBItem(movie: movie, index: 1)
}
