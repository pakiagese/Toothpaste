library(magick)

# Read image
img <- image_read("path/to/your/image.jpg") # Replace with path

# Convert to grayscale and apply edge detection
gray <- image_convert(img, colorspace = "gray")
edges <- image_canny(gray, geometry = "50%")

# Threshold and find contours (simplified)
binary <- image_threshold(edges, "50%")
blobs <- image_connected(binary, connected = 8)

# Count large blobs (toothpaste size)
blob_info <- image_get_region(blobs, geometry = "100x100+0+0") # Scan in patches
detections <- sum(sapply(blob_info, function(x) nrow(x) > 500)) # Filter large areas

# Annotate detections
if (detections > 0) {
  img_annotated <- image_annotate(img, paste("Detected", detections, "tubes"), location = "+10+10", color = "red")
} else {
  img_annotated <- img
}

# Display
plot(img_annotated)
cat("Detected", detections, "toothpaste tubes.\n")