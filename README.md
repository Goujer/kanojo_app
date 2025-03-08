# kanojo_app
 Barcode Kanojo App for Android

Please send me any backups, folders, .apks, .ipas, or screenshots you have of the original.

## How to connect to Goujer's server
* https = on
* url = 'kanojo.goujer.com'
* port = 443

### Special Instructions for devices running Android 5.0-6.0.1 (Can't get Android 4.X working this way for some reason)
Android devices of this age have difficulty with the modern Let's Encrypt certificates which Goujer's server uses.
These devices need to manually install the certificate authorities needed to connect.
* If your device does not have a password or pin set, you will need to set that up before these steps.
* Go to https://letsencrypt.org/certificates/ and download the **ISRG Root X1** self signed certificate .pem file. (Android 6 also allows .der files)
* Next, in your Android system settings, go to **Security**, Find Credential Storage, then **Install from Device Storage**.
* Find your downloaded .pem file and go through the on screen steps to name and install the file.
* Your app should now be able to connect to Goujer's server.
Note: You may see a notification about your activity being monitored while in the app. This is because the app is using a user installed certificate which in general is pretty sketchy, but in this instance we are installing a secure one that is newer than when the OS was built, so it's fine.

## Recovered Assets
### App Versions
| Version | Android (.apk) | iOS (.ipa) | Decrypted iOS (.ipa) |
|:--------|:--------------:|:----------:|:--------------------:|
| 2.4.2   |      Yes       |    Yes     |         Yes          |
| 2.4.1   |      Yes       |     No     |          No          |
| 2.0.4   |       No       |     No     |         Yes          |
| 1.3.3   |      Yes       |     No     |          No          |

### Assets Recovered
#### Live2D Parts
| Live2D Part | Live2D ID | Notes                        |
|-------------|-----------|------------------------------|
| Clothes     | 001       | 1 Button T-Shirt             |
| Clothes     | 002       | Sailor Fuku                  |
| Clothes     | 003       | Frill chest, tank-top        |
| Clothes     | 004       | Collared V-neck long sleeve  |
| Clothes     | 005       | Wide collared sweater        |
| Clothes     | 011       | White Sweater                |
| Clothes     | 022       | Red turtleneck sweater       |
| Clothes     | 050       | Black Sweater                |
| Clothes     | 051       | Spaghetti strap night-top    |
| Clothes     | 060       | Sleeping in bed              |
| Clothes     | 099       | Not a real asset             |
| Clothes     | 901       | Red flower-in-hair bikini    |
| Clothes     | 902       | Blue Axe bikini              |
| Clothes     | 903       | Red and yellow bikini        |
| Clothes     | 904       | Green Frill bikini           |
| Clothes     | 905       | Black and white frill bikini |
| Clothes     | 906       | Green and white frill bikini |
| Clothes     | 907       | Camo bikini                  |
| Clothes     | 908       | Green and pink flower kimono |
| Clothes     | 909       | Green and Yellow kimono      |
| Clothes     | 910       | Rose kimono                  |
| Clothes     | 911       | Pink hydrangea kimono        |
| Clothes     | 912       | Softbank T-Shirt             |
| Clothes     | 913       | Softbank Tank-top            |

Note: Live2D Clothes past 900 are collab outfits.

#### Backgrounds
| Description      | Name    | Usage Conditions             | Notes                                                                                |
|------------------|---------|------------------------------|--------------------------------------------------------------------------------------|
| Class room       | class   | For Married Kanojos possibly | Implemented with time of day changes, only for Kanojos currently wearing clothes 002 |
| Axe Resort       | Unknown | For Axe Body Spray Girls     | Implemented                                                                          |
| Axe Spring       | Unknown | For Axe Body Spray Girls     | Missing                                                                              |
| Recording Studio | Unknown | Likely a Softbank Collab     | Missing                                                                              |

#### Dates
Coming Soon

#### Gifts
Coming Soon

#### Shop Categories
Coming Soon
