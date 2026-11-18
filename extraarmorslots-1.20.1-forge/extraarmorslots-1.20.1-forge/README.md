# Extra Armor Slots (Minecraft 1.20.1 Forge)

`more armor slots` と同等の機能を備えた、Minecraft 1.20.1（Forge 47.2.0）向けのオリジナルModソースコード一式です。

## 主な機能
- **追加防具スロットGUI**: デフォルトで `O` キー（キー設定から変更可能）を押すと、追加防具スロット画面が開きます。
- **防御力・属性の反映**: 追加スロットに装備した防具の防御力（Armor）、防具強度（Armor Toughness）、ノックバック耐性などの属性値がプレイヤーに反映され、複数装備しても重複衝突せず加算されます。
- **設定ファイル対応**: `config/extraarmorslots-common.toml`（または `Configured` Mod）から以下を変更できます。
  - `extraSlotCount`: 追加スロット数（1〜27、デフォルト: 4）
  - `dropOnDeath`: 死亡時に追加スロットの防具をドロップするか（`keepInventory` 有効時は保持）

## ビルド手順（ローカル環境）
1. このZIPファイルを任意のフォルダに展開します。
2. **Java 17** がインストールされていることを確認します。
3. 展開したフォルダで以下のコマンドを実行します（Gradleがインストールされている場合は `gradle build`、または IntelliJ IDEA で `build.gradle` を開いて Gradle プロジェクトとしてインポートし、`build` タスクを実行してください）。
   ```bash
   gradle build
   ```
4. ビルド完了後、`build/libs/extraarmorslots-1.0.0.jar` が生成されるので、Minecraft 1.20.1（Forge）の `mods` フォルダに入れて使用してください。
