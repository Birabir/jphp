# XmlProcessor

- **class** `XmlProcessor` (`php\xml\XmlProcessor`) **extends** `Processor` (`php\format\Processor`)
- **source** `php/xml/XmlProcessor.php`

**Description**

Class XmlProcessor

---

#### Methods

- `->`[`format()`](#method-format)
- `->`[`formatTo()`](#method-formatto)
- `->`[`parse()`](#method-parse)
- `->`[`createDocument()`](#method-createdocument)

---
# Methods

<a name="method-format"></a>

### format()
```php
format(DomDocument $value): string
```

---

<a name="method-formatto"></a>

### formatTo()
```php
formatTo(DomDocument $value, php\io\Stream $output): void
```

---

<a name="method-parse"></a>

### parse()
```php
parse(Stream|string $string): DomDocument
```

---

<a name="method-createdocument"></a>

### createDocument()
```php
createDocument(): DomDocument
```