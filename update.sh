#!/usr/bin/env bash
export AWS_DEFAULT_PROFILE=ranjitiyer
mvn package
aws s3 cp /Users/ranjiti/work/alexa/AudioBooks/target/audiobooks-1.0.jar s3://audiobooks-uberjar
aws lambda update-function-code --function-name audioBookPlayer --s3-bucket audiobooks-uberjar --s3-key audiobooks-1.0.jar --region us-east-1
