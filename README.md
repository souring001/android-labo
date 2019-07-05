# ANDROID LABO
![android_labo](https://user-images.githubusercontent.com/29009733/60728648-c8ca3680-9f7b-11e9-9bb3-9aa1063896f9.jpg)

## つくる

### コントローラー
ダンボールとレーザー加工機でコントローラーを作ります。

### アプリ
傾き情報をUDPで送信するAndroidアプリを作ります。

このリポジトリがそのソースコードになります。
(https://github.com/souring001/android-labo)

### ゲーム
Unityで車のゲームを作ります。
UDPから送られてきた情報を、ゲーム内のハンドルとアクセルペダルの値に対応させます。

ソースコード
https://github.com/souring001/Unity3D-Cars

## わかる

* Android端末の傾き(Roll、Pitch, Azimut)は、加速度センサと磁気センサの値から`SensorManager.getOrientation`で計算することができる。

* UDPのパケットの中身はJSON形式担っており、コントローラーの種類(ペダル or ハンドル)と仰角を送信している。

```JSON
{
    "controller": "pedal",
    "pitch": -34
}

```



## あそぶ
ゲームとアプリを起動するだけです。


## 発明する
すでに実装済みですが、ペダルを踏んでいるときに`Vibrator`を使って振動させると、踏んでいるかのようないい感じのフィードバックを得られます。

## デコる
企業のステッカーなどを貼ると盛り上がると思います。
