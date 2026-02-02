# Git提交信息乱码问题说明

## 问题描述

提交 `87cc952` 的提交信息显示为乱码：
```
鏂囨。鏁寸悊锛氬悎骞跺綊绾虫墍鏈夋枃妗ｏ紝鍒犻櫎鍐椾綑鏂囨。锛屽彧淇濈暀鏍稿績鏂囨。
```

实际应该是：
```
文档整理：合并归纳所有文档，删除冗余文档，只保留核心文档
```

## 乱码原因

1. **Git未配置UTF-8编码**：提交时Git使用了系统默认编码（Windows通常是GBK），而不是UTF-8
2. **PowerShell编码问题**：PowerShell默认编码可能与Git不一致
3. **提交信息编码不匹配**：提交信息以GBK编码保存，但显示时按UTF-8解析，导致乱码

## 已完成的修复

### 1. 配置Git使用UTF-8编码

已执行以下配置（全局配置，对所有仓库生效）：

```bash
git config --global i18n.commitencoding utf-8      # 提交信息使用UTF-8
git config --global i18n.logoutputencoding utf-8  # 日志输出使用UTF-8
git config --global core.quotepath false          # 不转义路径中的特殊字符
```

### 2. 验证配置

检查配置是否生效：
```bash
git config --global --get i18n.commitencoding
git config --global --get i18n.logoutputencoding
```

## 修复已存在的乱码提交（可选）

### 方法1：使用git rebase修改提交信息（推荐）

**注意**：如果提交已推送到远程，修改后需要 `git push --force`，请谨慎操作！

```bash
# 1. 启动交互式rebase
git rebase -i ffc8461

# 2. 在编辑器中，将乱码提交前的 "pick" 改为 "reword"（或 "r"）
# 例如：
# pick 87cc952 乱码提交信息
# 改为：
# reword 87cc952 乱码提交信息

# 3. 保存并关闭编辑器，Git会打开新的编辑器让你修改提交信息
# 输入正确的提交信息：
# 文档整理：合并归纳所有文档，删除冗余文档，只保留核心文档

# 4. 保存并关闭，rebase完成

# 5. 如果已推送到远程，需要强制推送（谨慎！）
git push --force
```

### 方法2：创建新提交说明（安全，不修改历史）

如果不想修改历史，可以创建一个新提交来说明：

```bash
git commit --allow-empty -m "修复：更正提交信息编码问题

之前的提交信息因编码问题显示为乱码，实际内容为：
文档整理：合并归纳所有文档，删除冗余文档，只保留核心文档"
```

## 预防措施

### 1. 设置PowerShell编码为UTF-8

在PowerShell配置文件中添加（`$PROFILE`）：

```powershell
# 设置控制台输出编码为UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

# 设置环境变量
$env:LANG = "zh_CN.UTF-8"
```

### 2. 设置Git编辑器编码

如果使用VS Code作为Git编辑器：

```bash
git config --global core.editor "code --wait"
```

### 3. 提交前验证

提交前可以预览提交信息：
```bash
git commit -m "你的提交信息"
# 提交后立即查看
git log -1 --format="%s"
```

## 验证修复

检查最新的提交信息是否正确显示：

```bash
git log --oneline -5
```

如果所有中文提交信息都能正确显示，说明修复成功。

## 注意事项

1. **修改历史的风险**：如果提交已推送到远程仓库，修改历史需要 `git push --force`，这会影响其他协作者
2. **团队协作**：如果多人协作，修改历史前需要通知团队成员
3. **备份**：修改历史前建议创建备份分支：`git branch backup-before-rebase`

## 当前状态

- ✅ Git已配置为使用UTF-8编码
- ✅ 后续提交将使用UTF-8编码，不会再出现乱码
- ⚠️ 历史提交 `87cc952` 的乱码问题已记录，可根据需要选择是否修复

## 相关命令

```bash
# 查看Git编码配置
git config --global --list | Select-String encoding

# 查看提交历史
git log --oneline --all

# 查看特定提交的详细信息
git show 87cc952

# 修改最近的提交信息（如果是最新提交）
git commit --amend -m "正确的提交信息"
```
