% Load image
img = imread('path/to/your/image.jpg'); % Replace with path
imgGray = rgb2gray(img);

% Threshold for edges (adjust for toothpaste contrast)
bw = imbinarize(imgGray, 'adaptive');

% Clean up noise
bw = bwareaopen(bw, 100); % Remove small objects
se = strel('rectangle', [5, 5]);
bw = imclose(bw, se);

% Find connected components (blobs)
cc = bwconncomp(bw);
stats = regionprops(cc, 'BoundingBox', 'Area', 'Eccentricity');

% Filter for tube-like blobs (tall, thin, area > 500)
detections = 0;
figure; imshow(img);
hold on;
for i = 1:length(stats)
    bbox = stats(i).BoundingBox;
    area = stats(i).Area;
    if area > 500 && bbox(3)/bbox(4) < 0.5 % Width/height < 0.5 for tube shape
        rectangle('Position', bbox, 'EdgeColor', 'r', 'LineWidth', 2);
        detections = detections + 1;
    end
end
hold off;
title(['Detected ' num2str(detections) ' toothpaste tubes']);
fprintf('Detected %d toothpaste tubes.\n', detections);